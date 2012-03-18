package org.baksia.rustycage.wizards;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RustNewProjectPage extends WizardPage {
    private Text projectText;

    private IProject project;

    private ISelection selection;

    protected RustNewProjectPage(ISelection selection) {
        super("rustProjectWizard");
        setTitle("Rust project wizard");
        setDescription("Set Rust project name");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("&Project name:");

        projectText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectText.setLayoutData(gd);
        projectText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        initialize();
        dialogChanged();
        setControl(container);
    }

    private void initialize() {
        projectText.setText("RustProject");
    }

    public boolean createProject() {
        if (project != null) {
            IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
            try {
                project.create(desc, null);
                if (!project.isOpen()) {
                    project.open(null);
                }
                IFolder src = project.getFolder("src");
                if (!src.exists()) {
                    src.create(false, true, null);
                }
            } catch (CoreException e) {
                updateStatus(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private void dialogChanged() {
        project = ResourcesPlugin.getWorkspace().getRoot()
                .getProject(getProjectName());

        if (project.exists()) {
            updateStatus("Project already exists");
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getProjectName() {
        return projectText.getText();
    }

}

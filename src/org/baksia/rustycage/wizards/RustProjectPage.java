package org.baksia.rustycage.wizards;

import org.baksia.rustycage.Activator;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RustProjectPage extends WizardPage {
    private Text projectText;
    private Text version;
    private Text author;

    private IProject project;

    private Button isLib;

    private ISelection selection;

    protected RustProjectPage(ISelection selection) {
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
        layout.numColumns = 5;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("&Project name:");

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectText = new Text(container, SWT.BORDER | SWT.SINGLE);
        projectText.setLayoutData(gd);
        projectText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        isLib = new Button(container, SWT.CHECK);
        isLib.setText("Library");
        isLib.setLayoutData(gd);
        isLib.setSelection(true);

        Label lblVersion = new Label(container, SWT.NULL);
        lblVersion.setText("&Version :");



        version = new Text(container, SWT.BORDER | SWT.SINGLE);
        version.setLayoutData(gd);
        version.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        Label lblAuthor = new Label(container, SWT.NULL);
        lblAuthor.setText("&Author:");


        author = new Text(container, SWT.BORDER | SWT.SINGLE);
        author.setLayoutData(gd);
        author.addModifyListener(new ModifyListener() {
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
        projectText.setText("rust_project");
    }

    public boolean createProject() {
        if (project != null) {
            IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
            preferenceStore.setValue("ProjectName", project.getName());
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
                IFile file = src.getFile(project.getName() + ".rc");
                if (!file.exists()) {
                    file.create(openContentStream(), true, null);
                }
            } catch (CoreException e) {
                updateStatus(e.getMessage());
                return false;
            }
        }
        return true;
    }


    private InputStream openContentStream() {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("#[link(name = \"")
                .append(project.getName())
                .append("\", vers = \"")
                .append(version.getText())
                .append("\", author = \"")
                .append(author.getText())
                .append("\")];\n");
        if (isLib.getSelection()) {
            contentBuilder.append("#[crate_type = \"lib\"];");
        }

        return new ByteArrayInputStream(contentBuilder.toString().getBytes());
    }

    private void dialogChanged() {
        if (getProjectName().isEmpty()) {
            return;
        }
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

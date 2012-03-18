package org.baksia.rustycage.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RustNewProjectPage extends WizardPage {
    private Text projectText;

    private Text rustHomeText;

    private ISelection selection;

    protected RustNewProjectPage(ISelection selection) {
        super("rustProjectWizard");
        setTitle("Rust project wizard");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
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

        //TODO: Is there a spacer?
        label = new Label(container, SWT.NULL);
        label = new Label(container, SWT.NULL);
        label.setText("&Rust home:");

        rustHomeText = new Text(container, SWT.BORDER | SWT.SINGLE);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        rustHomeText.setLayoutData(gd);
        rustHomeText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    private void initialize() {
        projectText.setText("NewProject");
        rustHomeText.setText("$RUST_HOME");
    }

    private void handleBrowse() {

    }

    private void dialogChanged() {
        IResource container = ResourcesPlugin.getWorkspace().getRoot()
                .findMember(new Path(getProjectName()));
        String fileName = getRustHome();

        if (getProjectName().length() == 0) {
            updateStatus("Project name must be specified");
            return;
        }

        //container.getWorkspace().sortNatureSet()
//        || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0
//        if (container == null) {
//            updateStatus("Project name can not be empty");
//            return;
//        }
//        if (!container.isAccessible()) {
//            updateStatus("Project must be writable");
//            return;
//        }
//        if (fileName.length() == 0) {
//            updateStatus("File name must be specified");
//            return;
//        }
//        if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
//            updateStatus("File name must be valid");
//            return;
//        }
        int dotLoc = fileName.lastIndexOf('.');
        if (dotLoc != -1) {
            String ext = fileName.substring(dotLoc + 1);
            if (ext.equalsIgnoreCase("rs") == false) {
                updateStatus("File extension must be \"rs\"");
                return;
            }
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

    public String getRustHome() {
        return rustHomeText.getText();
    }
}

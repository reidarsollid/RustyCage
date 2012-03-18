package org.baksia.rustycage.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class RustNewProjectWizard extends Wizard implements INewWizard {

    private RustNewProjectPage rustNewProjectPage;
    private ISelection selection;


    public RustNewProjectWizard() {
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        rustNewProjectPage = new RustNewProjectPage(selection);
        addPage(rustNewProjectPage);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {

    }

    @Override
    public boolean performFinish() {
        return rustNewProjectPage.createProject();
    }

}

package org.baksia.rustycage.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class RustProjectWizard extends Wizard implements INewWizard {

    private RustProjectPage rustProjectPage;
    private ISelection selection;


    public RustProjectWizard() {
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        rustProjectPage = new RustProjectPage(selection);
        addPage(rustProjectPage);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    @Override
    public boolean performFinish() {
        return rustProjectPage.createProject();
    }

}

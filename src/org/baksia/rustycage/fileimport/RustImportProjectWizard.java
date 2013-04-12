package org.baksia.rustycage.fileimport;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class RustImportProjectWizard extends Wizard implements IImportWizard {
    private RustImportProjectPage rustImportProjectPage;

    public RustImportProjectWizard() {

    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {

    }

    @Override
    public boolean performFinish() {

        return false;
    }

}

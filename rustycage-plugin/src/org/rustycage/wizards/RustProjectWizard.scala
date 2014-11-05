package org.rustycage.wizards

import org.eclipse.jface.viewers.{ISelection, IStructuredSelection}
import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui.{INewWizard, IWorkbench}

class RustProjectWizard extends Wizard with INewWizard {
  setNeedsProgressMonitor(true)

  override def addPages() {
    rustProjectPage = new RustProjectPage(selection)
    addPage(rustProjectPage)
  }

  override def init(workbench: IWorkbench, selection: IStructuredSelection) {
    RustProjectWizard.this.selection = selection
  }

  override def performFinish(): Boolean = rustProjectPage.createProject()

  private var rustProjectPage: RustProjectPage = _
  private var selection: ISelection = _
}
package org.baksia.rustycage.wizards

import org.eclipse.jface.wizard.Wizard
import org.eclipse.jface.dialogs.IDialogSettings
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.INewWizard
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IWorkbench
import org.eclipse.jface.viewers.ISelection

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
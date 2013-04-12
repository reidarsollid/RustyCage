package org.baksia.rustycage.fileimport

import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui.IImportWizard
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IWorkbench

class RustImportProjectWizard extends Wizard with IImportWizard {
  val rustImportProjectPage: RustImportProjectPage = new RustImportProjectPage()

  def init(workbench: IWorkbench, selection: IStructuredSelection) {
    setWindowTitle("Rust Project Import Wizard")
    setNeedsProgressMonitor(true)
  }

  def performFinish(): Boolean = false

  override def addPages() {
    super.addPages()
    addPage(rustImportProjectPage)
  }
}

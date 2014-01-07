package org.rustycage.fileimport

import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui.{IWorkbench, IImportWizard}
import org.eclipse.jface.viewers.IStructuredSelection


class RustImportProjectWizard extends Wizard with IImportWizard {
  val rustImportProjectPage: RustImportProjectPage = new RustImportProjectPage()

  def init(workbench: IWorkbench, selection: IStructuredSelection) {
    setWindowTitle("Rust Project Import Wizard")
    setNeedsProgressMonitor(true)
  }

  def performFinish(): Boolean = true

  override def addPages() {
    super.addPages()
    addPage(rustImportProjectPage)
  }
}

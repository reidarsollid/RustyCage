package org.rustycage.fileimport


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

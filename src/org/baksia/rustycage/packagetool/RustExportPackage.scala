package org.baksia.rustycage.packagetool

import org.eclipse.jface.wizard.Wizard
import org.eclipse.jface.viewers.{ISelection, IStructuredSelection}
import org.eclipse.ui.IExportWizard
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IWorkbench

class RustExportPackage extends Wizard with IExportWizard {
  setNeedsProgressMonitor(true)
  
  def init(workbench: IWorkbench, selection: IStructuredSelection) {
    this.selection = selection
    this.workbench = workbench
  }

  override def addPages() {
    addPage(new RustExportPackageWizardPage(workbench))
  }

  def performFinish(): Boolean = true

  private var selection: IStructuredSelection = _
  private var workbench: IWorkbench = _		
}
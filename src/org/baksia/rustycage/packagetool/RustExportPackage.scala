package org.baksia.rustycage.packagetool

import org.eclipse.jface.wizard.Wizard
import org.eclipse.jface.dialogs.IDialogSettings
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IExportWizard
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IWorkbench

class RustExportPackage extends Wizard with IExportWizard {
	def init(workbench: IWorkbench ,  selection: IStructuredSelection) {
		addPage(new RustExportPackageWizardPage(workbench))
	}
	
	def performFinish(): Boolean = {
	  true
	}
}
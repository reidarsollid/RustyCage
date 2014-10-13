package org.rustycage.wizards

import org.eclipse.ui.INewWizard
import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui.IWorkbench
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ISelection

class RustNewCargoProjectWizard extends Wizard with INewWizard {
  
    private var selection: ISelection = _
  
    override def init(workbench: IWorkbench, selection: IStructuredSelection) {
      this.selection = selection;
    }
    
    override def performFinish(): Boolean = {
      
      return false;
    }
  
}
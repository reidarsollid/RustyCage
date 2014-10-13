package org.rustycage.packagetool

import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.jface.preference.FileFieldEditor
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.core.runtime.Path
import org.rustycage.RustPlugin
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.ui.IWorkbench

class RustExportPackageWizardPage(workBench: IWorkbench) extends WizardPage("Export Package Wizard") {
  setTitle("Select crate file")
  
  override def createControl(parent: Composite) {
    
    val fileSelectionArea = new Composite(parent, SWT.NONE)
    val fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL)
    fileSelectionArea.setLayoutData(fileSelectionData)

    val fileSelectionLayout = new GridLayout()
    fileSelectionLayout.numColumns = 3
    fileSelectionLayout.makeColumnsEqualWidth = false
    fileSelectionLayout.marginWidth = 0
    fileSelectionLayout.marginHeight = 0
    fileSelectionArea.setLayout(fileSelectionLayout)
    
  }
  
  //Rust hasn't supported .rc files in a long while, there for this code is very unnecessary and needs to be re-written.
  //More about it: https://github.com/brson/rust/commit/be6613e048c889a0aeaff056131c2406259f1fb4
  
  /*override def createControl(parent: Composite) {
    val fileSelectionArea = new Composite(parent, SWT.NONE)
    val fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL)
    fileSelectionArea.setLayoutData(fileSelectionData)

    val fileSelectionLayout = new GridLayout()
    fileSelectionLayout.numColumns = 3
    fileSelectionLayout.makeColumnsEqualWidth = false
    fileSelectionLayout.marginWidth = 0
    fileSelectionLayout.marginHeight = 0
    fileSelectionArea.setLayout(fileSelectionLayout)

    val projectName = RustPlugin.prefStore.getString("ProjectName")
    val editor = new FileFieldEditor("fileSelect", "Select crate: ", false, fileSelectionArea)
    val workspaceRoot = ResourcesPlugin.getWorkspace.getRoot
    val projectURI = workspaceRoot.getLocationURI.getPath
    val projects = workspaceRoot.getProjects
    val srcFolders = projects.foreach(project => 
    	project.getFolder("src")
    )
   // val projectRoot = new File(projectURI.toString())
    //editor.setFilterPath(projectRoot)
    editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {
      def modifyText(e: ModifyEvent) {
        val path = new Path(editor.getStringValue)
        // setFileName(path.lastSegment())
      }
    })

    val extensions = Array[String]("*.rc")
    editor.setFileExtensions(extensions)
    fileSelectionArea.moveAbove(null)
    setControl(fileSelectionArea)
  }*/
}
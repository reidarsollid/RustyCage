package org.baksia.rustycage.packagetool

import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.jface.preference.FileFieldEditor
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.core.runtime.Path
import org.baksia.rustycage.RustPlugin
import org.eclipse.core.resources.ResourcesPlugin

class RustExportPackageWizardPage extends WizardPage("Export Package Wizard") {
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

    val projectName = RustPlugin.prefStore.getString("ProjectName")
    val filterPath = ResourcesPlugin.getWorkspace.getRoot.getProject(projectName).getFolder("").getFullPath.toFile
    val editor = new FileFieldEditor("fileSelect", "Select crate: ",true, fileSelectionArea)
    editor.setFilterPath(filterPath)
    editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {
      def modifyText(e: ModifyEvent) {
        val path = new Path(editor.getStringValue)
        // setFileName(path.lastSegment())
      }
    })

    val extensions = Array[String]("*.rc")
    editor.setFileExtensions(extensions)
    fileSelectionArea.moveAbove(null)
  }
}
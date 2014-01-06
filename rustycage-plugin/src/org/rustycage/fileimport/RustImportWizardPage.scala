package org.rustycage.fileimport

import com.codeminders.scalaws.helpers.io.SourceInputStream
import scala.io._

class RustImportWizardPage(pageName: String, selection: IStructuredSelection) extends WizardNewFileCreationPage(pageName, selection) {
  protected var editor: FileFieldEditor = null

  setTitle(pageName)
  setDescription("Import a file from the local file system into the workspace")

  override def createAdvancedControls(parent: Composite) {
    val fileSelectionArea = new Composite(parent, SWT.NONE)
    val fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL)
    fileSelectionArea.setLayoutData(fileSelectionData)

    val fileSelectionLayout = new GridLayout()
    fileSelectionLayout.numColumns = 3
    fileSelectionLayout.makeColumnsEqualWidth = false
    fileSelectionLayout.marginWidth = 0
    fileSelectionLayout.marginHeight = 0
    fileSelectionArea.setLayout(fileSelectionLayout)

    editor = new FileFieldEditor("fileSelect", "Select File: ", fileSelectionArea)
    editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener() {
      def modifyText(e: ModifyEvent) {
        val path = new Path(editor.getStringValue)
        setFileName(path.lastSegment())
      }
    })

    val extensions = Array[String]("*.rs", "*.rc")
    editor.setFileExtensions(extensions)
    fileSelectionArea.moveAbove(null)
  }

  override def createLinkTarget() {
    // do nothing, we don't have this section
  }

  override protected def getInitialContents = new SourceInputStream(Source.fromFile(editor.getStringValue))

  override protected def getNewFileLabel = "New File Name:"

  override protected def validateLinkedResource(): IStatus = new Status(IStatus.OK, "RustyCage", IStatus.OK, "", null)

}

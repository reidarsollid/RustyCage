package org.baksia.rustycage.wizards

import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.viewers.ISelection
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IContainer
import org.eclipse.ui.dialogs.ContainerSelectionDialog
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jface.window.Window
import org.eclipse.core.runtime.Path

class RustNewFileWizardPage(selection: ISelection) extends WizardPage("New file wizard") {
  setTitle("Rust File")
  setDescription("This wizard creates a new file with *.rs and *.rc extension that can be opened by a Rust editor.")

  override def createControl(parent: Composite) {
    val container = new Composite(parent, SWT.NULL)
    val layout = new GridLayout()
    container.setLayout(layout)
    layout.numColumns = 3
    layout.verticalSpacing = 9
    var label = new Label(container, SWT.NULL)
    label.setText("&Container:")

    containerText = new Text(container, SWT.BORDER | SWT.SINGLE)
    val gd = new GridData(GridData.FILL_HORIZONTAL)
    containerText.setLayoutData(gd)
    containerText.addModifyListener(new ModifyListener() {

      def modifyText(e: ModifyEvent) {
        dialogChanged()
      }
    })

    val button = new Button(container, SWT.PUSH)
    button.setText("Browse...")
    button.addSelectionListener(new SelectionAdapter() {

      override def widgetSelected(e: SelectionEvent) {
        handleBrowse()
      }
    })
    label = new Label(container, SWT.NULL)
    label.setText("&File name:")

    fileText = new Text(container, SWT.BORDER | SWT.SINGLE)
    fileText.setLayoutData(gd)
    fileText.addModifyListener(new ModifyListener() {
      def modifyText(e: ModifyEvent) {
        dialogChanged()
      }
    })
    initialize()
    dialogChanged()
    setControl(container)
  }

  def getContainerName: String = containerText.getText


  def getFileName: String = fileText.getText

  private def initialize() {
    if (selection != null && !selection.isEmpty
      && selection.isInstanceOf[IStructuredSelection]) {
      val ssel = selection.asInstanceOf[IStructuredSelection]
      if (ssel.size() > 1)
        return
      val container = ssel.getFirstElement match {
        case obj: IContainer =>
          obj
        case obj: IResource =>
          obj.getParent
      }
      containerText.setText(container.getFullPath.toString)
    }
    fileText.setText("new_file.rs")
  }

  private def handleBrowse() {
    val dialog = new ContainerSelectionDialog(
      getShell, ResourcesPlugin.getWorkspace.getRoot, false, "Select new file container")
    if (dialog.open() == Window.OK) {
      val result = dialog.getResult
      if (result.length == 1) {
        containerText.setText(result(0).asInstanceOf[Path].toString)
      }
    }
  }

  private def dialogChanged() {
    val container = ResourcesPlugin.getWorkspace.getRoot
      .findMember(new Path(getContainerName))
    val fileName = getFileName

    if (getContainerName.length() == 0) {
      updateStatus("File container must be specified")
      return
    }
    if (container == null
      || (container.getType & (IResource.PROJECT | IResource.FOLDER)) == 0) {
      updateStatus("File container must exist")
      return
    }
    if (!container.isAccessible) {
      updateStatus("Project must be writable")
      return
    }
    if (fileName.length() == 0) {
      updateStatus("File name must be specified")
      return
    }
    if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
      updateStatus("File name must be valid")
      return
    }
    val dotLoc = fileName.lastIndexOf('.')
    if (dotLoc != -1) {
      val ext = fileName.substring(dotLoc + 1)
      if (!ext.equalsIgnoreCase("rs") && !ext.equalsIgnoreCase("rc")) {
        updateStatus("File extension must be \"rs\" or \"rc\"")
        return
      }
    } else {
      updateStatus("File extension missing \"rs\" or \"rc\"")
      return
    }
    updateStatus(null)
  }

  private def updateStatus(message: String) {
    setErrorMessage(message)
    setPageComplete(message == null)
  }

  private var containerText: Text = _

  private var fileText: Text = _
}
package org.rustycage.wizards

import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Text
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import java.io.InputStream
import java.io.ByteArrayInputStream
import org.rustycage.RustPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.widgets.Button
import org.eclipse.jface.viewers.ISelection

class RustProjectPage(selection: ISelection) extends WizardPage("Rust project wizard") {
  setTitle("Rust project wizard")
  setDescription("Set Rust project name")

  override def createControl(parent: Composite) {
    val container = new Composite(parent, SWT.NULL)
    val layout = new GridLayout()
    container.setLayout(layout)
    layout.numColumns = 5
    layout.verticalSpacing = 9
    val label = new Label(container, SWT.NULL)
    label.setText("&Project name:")

    val gd = new GridData(GridData.FILL_HORIZONTAL)
    projectText = new Text(container, SWT.BORDER | SWT.SINGLE)
    projectText.setLayoutData(gd)
    projectText.addModifyListener(new ModifyListener() {

      override def modifyText(e: ModifyEvent) {
        dialogChanged()
      }
    })

    isLib = new Button(container, SWT.CHECK)
    isLib.setText("Library")
    isLib.setLayoutData(gd)
    isLib.setSelection(true)

    val lblVersion = new Label(container, SWT.NULL)
    lblVersion.setText("&Version :")

    version = new Text(container, SWT.BORDER | SWT.SINGLE)
    version.setLayoutData(gd)
    version.addModifyListener(new ModifyListener() {

      override def modifyText(e: ModifyEvent) {
        dialogChanged()
      }
    })

    initialize()
    dialogChanged()
    setControl(container)
  }

  private def initialize() {
    projectText.setText("rust_project")
  }


  def createProject(): Boolean = {
    if (project != null) {
      //TODO: Change to project preferences
      val preferenceStore = RustPlugin.prefStore
      preferenceStore.setValue("ProjectName", project.getName)
      preferenceStore.setValue("IsLib", isLib.getSelection)
      val desc = project.getWorkspace.newProjectDescription(project.getName)
      try {
        project.create(desc, null)
        if (!project.isOpen) {
          project.open(null)
        }
        val src = project.getFolder("src")
        if (!src.exists()) {
          src.create(false, true, null)
        }
        val test = project.getFolder("test")
        if (!test.exists()) {
          test.create(false, true, null)
        }
        val bin = project.getFolder("bin")
        if (!bin.exists()) {
          bin.create(false, true, null)
        }
        bin.setHidden(true)
        val srcCrate = src.getFile(project.getName + ".rc")
        if (!srcCrate.exists()) {
          srcCrate.create(openContentStream(), true, null)
        }
        val testCrate = test.getFile("test_" + project.getName + ".rc")
        if (!testCrate.exists()) {
          testCrate.create(openTestContentStream(), true, null)
        }
      } catch {
        case e: CoreException =>
          updateStatus(e.getMessage)
          false
      }
    }
    true
  }

  private def openContentStream(): InputStream = {
    val contentBuilder = new StringBuilder()
    contentBuilder.append("#[crate_id = \"")
      .append(project.getName)
      .append("#")
      .append(version.getText)
      .append("\"];\n")
    if (isLib.getSelection) {
      contentBuilder.append("#[crate_type = \"lib\"];")
    }
    new ByteArrayInputStream(contentBuilder.toString().getBytes)
  }

  def openTestContentStream() : InputStream = {
    val contentBuilder = new StringBuilder()
    contentBuilder.append("#[crate_id = \"")
      .append(project.getName)
      .append("#")
      .append(version.getText)
      .append("\"];\n")
    contentBuilder.append("#[crate_type = \"test\"];")
    new ByteArrayInputStream(contentBuilder.toString().getBytes)
  }

  private def dialogChanged() {
    if (getProjectName.isEmpty) {
      return
    }
    project = ResourcesPlugin.getWorkspace.getRoot
      .getProject(getProjectName)

    if (project.exists()) {
      updateStatus("Project already exists")
      return
    }
    updateStatus(null)
  }

  private def updateStatus(message: String) {
    setErrorMessage(message)
    setPageComplete(message == null)
  }

  def getProjectName: String = projectText.getText

  private var projectText: Text = _
  private var version: Text = _
  private var author: Text = _
  private var project: IProject = _
  private var isLib: Button = _

}
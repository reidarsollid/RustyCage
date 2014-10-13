package org.rustycage.wizards

import org.eclipse.core.resources.IProject
import org.eclipse.core.internal.resources.File
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Button
import java.io.InputStream
import java.io.ByteArrayInputStream
import org.eclipse.core.runtime.Path
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.widgets.Text
import org.eclipse.core.resources.ResourcesPlugin
import org.rustycage.RustPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.resources.IFile

class RustNewCargoProjectPage (selection: ISelection) extends WizardPage("New cargo project wizard") {
    setTitle("New Cargo project");
    setDescription("Creates a new project which uses Cargo build manager");
    
    override def createControl(parent: Composite) {
      val container = new Composite(parent, SWT.NULL)
      val layout = new GridLayout()
      val gd = new GridData(GridData.FILL_HORIZONTAL)
     
      /* I think this needs refining */
      container.setLayout(layout)
      layout.numColumns = 5
      layout.verticalSpacing = 9
      
      val projectLabel = new Label(container, SWT.NULL)
      projectLabel.setText("&Project name: ")
      
      /* Initialize Text objects */
      
      projectName = new Text(container, SWT.BORDER | SWT.SINGLE)
      projectName.setLayoutData(gd)
      projectName.addModifyListener(new ModifyListener() {
        override def modifyText(e: ModifyEvent) {
          dialogChanged(projectName)
        }
      })
      
      val versionLabel = new Label(container, SWT.NULL)
      versionLabel.setText("&Version: ")
      
      version = new Text(container, SWT.BORDER | SWT.SINGLE)
      version.setLayoutData(gd)
      version.addModifyListener(new ModifyListener() {
        override def modifyText(e: ModifyEvent) {
          dialogChanged(version)
        }
      })
      
      val authorsLabel = new Label(container, SWT.NULL)
      authorsLabel.setText("&Authors: ")
      
      authors = new Text(container, SWT.BORDER | SWT.SINGLE)
      authors.setLayoutData(gd)
      version.addModifyListener(new ModifyListener() {
        override def modifyText(e: ModifyEvent) {
          dialogChanged(version)
        }
      })
      
      initialize()
      setControl(container)
    }
    
    def initialize() {
      projectName.setText("project_name")
      version.setText("0.0.1")
      authors.setText("Example Name <example@email.com>")
    }
    
    def dialogChanged(text: Text) {
      if (text.getText.isEmpty) {
        return
      }
      
      project = ResourcesPlugin.getWorkspace.getRoot.getProject(projectName.getText())
      
      /* Exception cases */
      if (text == projectName && project.exists()) {
        updateStatus("Project already exists")
        return
      }
      
      if(text == version && text.getText().isEmpty()) {
        updateStatus("Version can't be blank")
        return
      }
      
      if(text == authors) {
        
      }
      
      updateStatus(null)
    }
    
    private def updateStatus(message: String) {
      setErrorMessage(message)
      setPageComplete(message == null)
    }
    
    def createProject() : Boolean = {
      if(project != null) {
        val prefStore = RustPlugin.prefStore
        prefStore.setValue("ProjectName", projectName.getText)
        val desc = project.getWorkspace().newProjectDescription(project.getName)
        try {
        project.create(desc, null)
        if(!project.isOpen()) {    
          project.open(null)
        }
        val src = project.getFolder("src")
        if(!src.exists()) {
          src.create(false, true, null)
        }
        val cargoFile = project.getFile("Cargo.toml")
        if(!cargoFile.exists) {
          cargoFile.create(createTomlFile(), true, null)
        }
        mainFile = src.getFile("main.rs")
        if(!mainFile.exists()) {
          mainFile.create(createBasicRustFile(), true, null)
        }
        
        } catch {
          case e: CoreException =>
            updateStatus(e.getMessage)
            false
        }
      }
      false
    }
    
    private def createBasicRustFile(): InputStream = {
      val contentBuilder = new StringBuilder()
                        .append("mod main;")
                        .append(System.getProperty("line.separator"))
                        .append("fn main() {")
                        .append("    println!(\"Hello, World!\")")
                        .append("}")
      new ByteArrayInputStream(contentBuilder.toString.getBytes)
    }
    
    def createTomlFile() : InputStream =  {
      val contentBuilder = new StringBuilder()
                        .append("[project]")
                        .append(System.getProperty("line.separator"))
                        .append("name = \"" + projectName + "\"")
                        .append("version = \"" + version + "\"")
          if(hasAuthors.getSelection) {
            contentBuilder.append("authors = ")
          }
          if(hasReadme.getSelection) {
            contentBuilder.append("readme = \"" + createReadme + "\"")
          }
          contentBuilder.append(System.getProperty("line.separator"))
                        .append("[[bin]]")
                        .append(System.getProperty("line.separator"))
                        .append("name = \"" + mainFile.getName() + "\"")
                        .append("path = \"" + mainFile.getLocation() + "\"")
      new ByteArrayInputStream(contentBuilder.toString().getBytes)
    }
    
    def createReadme(): String = {
      val readme_string: String = "README.md"
      val readme = project.getFile(readme_string)
      
      if(!readme.exists())
        readme.create(readmeStream(), false, null);
      return readme_string
    }
    
    def readmeStream(): InputStream = {
      val contentBuilder = new StringBuilder()
                        .append(projectName)
      new ByteArrayInputStream(contentBuilder.toString().getBytes)
    }
    
    var mainFile: IFile = _
    
    var project: IProject = _
    var projectName: Text = _
    var version: Text = _
    
    var authors: Text = _
    var hasAuthors: Button = _
    
    var library: Button = _
    var hasReadme: Button = _
}
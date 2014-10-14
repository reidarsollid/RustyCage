package org.rustycage.run

import org.eclipse.core.commands.{ExecutionEvent, AbstractHandler}
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.core.resources.{IFile, IContainer, IProject}
import org.rustycage.editors.RustEditor
import org.eclipse.jface.preference.IPreferenceStore
import org.rustycage.RustPlugin
import org.eclipse.ui.internal.Workbench
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.core.resources.IResource
import org.eclipse.jface.text.ITextSelection


class RustRunner extends AbstractHandler {

  override def execute(event: ExecutionEvent): AnyRef = {
    run(getActiveProject)
    
    null
  }

  import scala.sys.process._
  def run(project: IProject) {
    val bin = project.getFolder("bin")
    val messageConsole = new MessageConsoleScala(findExecutable(bin, project), "Run : ")
    messageConsole.message("Running: " + findExecutable(bin, project))
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))

    findExecutable(bin, project).getLocationURI().getRawPath() ! logger
    messageConsole.close()

  }

  def getActiveProject(): IProject = {
    val selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService()
    val selection = selectionService.getSelection()
    
    if(selection.isInstanceOf[IStructuredSelection]) {
      val element: Object = selection.asInstanceOf[IStructuredSelection].getFirstElement()
      
      if(element.isInstanceOf[IResource]) {
        val project = element.asInstanceOf[IResource].getProject()
        project
      } else {
        throw new RuntimeException("No project could be found!")
      }
      
    }
    null
  }
  
  def findExecutable(dir: IContainer, project: IProject): IFile = {
    val prefStore: IPreferenceStore = RustPlugin.prefStore
    
    if(!prefStore.getString("RUNTIME_ARGS").contains("--out-dir")) {
      val projectFiles = dir.members().filter(r => r.getFileExtension == ".exe")
      if(projectFiles(0) != null) projectFiles(0)
    }
    
    val files = dir.members().filter(r => r.getFileExtension == ".exe")
    if(files(0) != null) {
      files(0)
    }
    throw new RuntimeException("Bin folder should only consist of one executable")
  }
  

}

package org.rustycage.run

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.jface.preference.IPreferenceStore
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.internal.Workbench
import org.rustycage.RustPlugin
import org.rustycage.editors.RustEditor


class RustRunner extends AbstractHandler {

  override def execute(event: ExecutionEvent): AnyRef = {
    run(getActiveProject)
    
    null
  }

  import scala.sys.process._
  def run(project: IProject) {
    val messageConsole = new MessageConsoleScala(findExecutable.asInstanceOf[IFile], "Run : ")
    messageConsole.message("Running: " + findExecutable)
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))

    findExecutable ! logger
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
  
  import scala.util.control.Breaks._
  def findExecutable(): String = {
    var outDir: String = ""
    
    val prefStore: IPreferenceStore = RustPlugin.prefStore
    val runtime = prefStore.getString("RUNTIME_ARGS").split(" ")
    
    for(i <- 0 to runtime.length) {
      if(runtime(i) == "--output-dir") {
        outDir = runtime(i + 1)
        break;
      }
    }
    
    if(!outDir.isEmpty()) {
      val executable = getActiveProject.getFolder(outDir).members()
                                       .filter(r => r.getFileExtension() == ".exe")
      if(executable.length > 0)
        executable(0)
    }
    
    val root = getActiveProject.members().filter(r => r.getFileExtension() == ".exe")
    
    if(root.length > 0)
      root(0)
    
    throw new RuntimeException("Bin folder should only consist of one executable")
  }
  

}

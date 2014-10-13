package org.rustycage.compile

import org.eclipse.jface.preference.IPreferenceStore
import org.rustycage.RustPlugin
import org.rustycage.RustPreferenceConstants
import java.io.IOException
import org.eclipse.core.resources.{IProject, IFolder, ResourcesPlugin, IFile}
import org.eclipse.core.runtime.{IPath, IProgressMonitor, CoreException}
import org.rustycage.run.MessageConsoleScala
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.IContainer
import org.eclipse.core.runtime.Path
import org.eclipse.core.resources.IResource

object RustyCageCompile {

  import scala.sys.process._
  
  def compile(arguments: String, monitor: IProgressMonitor, project: IProject): Boolean = {
    try {
      val preferenceStore: IPreferenceStore = RustPlugin.prefStore
      val rustPath: String = preferenceStore.getString(RustPreferenceConstants.RUST_C)
      
      val projectName: String = project.getName
      val rawPath: String = project.getRawLocationURI().getPath()
      
      var src: IFolder = project.getFolder("src")
      var bin: IFolder = project.getFolder("bin")
      
      val execCompile = rustPath + "rustc " + findMainFile(src) + " " + arguments + " " + bin
      val messageConsole = new MessageConsoleScala(findMainFile(src).asInstanceOf[IFile], "Compile : ") with ProblemMarker
      messageConsole.message("Compiling")
      val logger = ProcessLogger(
        (o: String) => messageConsole.message(o),
        (e: String) => messageConsole.errorMessage(e))

      execCompile ! logger
      true
    }
    catch {
      case e: IOException => {
        false
      }
      case e: CoreException => {
        false
      }
    }
  }
  
  def findMainFile(dir: IFolder): String = {
    val main = dir.getFile("main.rs")
    if(main.exists()) {
      main
    } else {
      throw new RuntimeException("No main file found")
    }
    throw new RuntimeException
  }

  
}

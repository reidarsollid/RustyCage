package org.rustycage.compile

import org.eclipse.jface.preference.IPreferenceStore
import org.rustycage.RustPlugin
import org.rustycage.preferences.PreferenceConstants
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

  def compile(crate: IResource, argument: String, monitor: IProgressMonitor, project: IProject): Boolean = {
    try {
      val preferenceStore: IPreferenceStore = RustPlugin.prefStore
      val rustPath: String = preferenceStore.getString(PreferenceConstants.RUST_C)
      
      val projectName: String = project.getName
      val rawPath: String = crate.getRawLocationURI.getRawPath
      val srcPath: IPath = crate.getFullPath
      
     /// val cratePath = fullPath.removeFileExtension().removeFirstSegments(1).append(projectName).addFileExtension("rc")
      val endIndex: Int = rawPath.indexOf(projectName)
      var src: String = ""
      var bin: String = ""
      if (endIndex != -1) {
        val home: String = rawPath.substring(0, endIndex)
        bin = home + projectName + "/bin"
        //TODO : Fix me path and name
        src = " -L " + home + projectName + "/bin"
      }
     /* val project: IProject = file.getProject
      if (!project.isOpen) {
        project.open(null)
      }
      val binFolder: IFolder = project.getFolder("bin")
      if (!binFolder.exists()) {
        binFolder.create(false, true, null)
      }
      binFolder.setHidden(true)*/
      //rustc --test -L ../bin hello_test.rc
      val execCompile = rustPath + "rustc " + argument + rawPath + src + " --out-dir " + bin
      val messageConsole = new MessageConsoleScala(crate.asInstanceOf[IFile], "Compile : ") with ProblemMarker
      messageConsole.message("Compiling: " + crate)	
      val logger = ProcessLogger(
        (o: String) => messageConsole.message(o),
        (e: String) => messageConsole.errorMessage(e))

      execCompile ! logger
      true
    }
    catch {
      case e: IOException => {
        return false
      }
      case e: CoreException => {
        return false
      }
    }
  }
  
}

package org.baksia.rustycage.compile

import org.eclipse.jface.preference.IPreferenceStore
import org.baksia.rustycage.RustPlugin
import org.baksia.rustycage.preferences.PreferenceConstants
import java.io.IOException
import org.eclipse.core.resources.{IProject, IFolder, ResourcesPlugin, IFile}
import org.eclipse.core.runtime.{IProgressMonitor, CoreException}
import org.baksia.rustycage.run.MessageConsoleScala

object RustyCageCompile {

  import scala.sys.process._

  def compile(file: IFile, argument: String, monitor: IProgressMonitor): Boolean = {
    try {
      val preferenceStore: IPreferenceStore = RustPlugin.prefStore
      val rustPath: String = preferenceStore.getString(PreferenceConstants.RUST_C)
      val projectName: String = preferenceStore.getString("ProjectName")
      val rawPath: String = file.getRawLocationURI.getRawPath
      val endIndex: Int = rawPath.indexOf(projectName)
      var src: String = ""
      var bin: String = ""
      if (endIndex != -1) {
        val home: String = rawPath.substring(0, endIndex)
        bin = home + projectName + "/bin"
        src = " -L " + home + projectName + "/src"
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
      val execCompile = rustPath + "rustc " + argument + rawPath + src + " --out-dir " + bin
      val messageConsole = new MessageConsoleScala(file, "Compile : ") with ProblemMarker

      val logger = ProcessLogger(
        (o: String) => messageConsole.message(o),
        (e: String) => messageConsole.errorMessage(e))

      messageConsole.message("<<<<Done>>>>")
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

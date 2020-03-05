package org.rustycage.compile

import java.io.IOException
import java.nio.file.{Files, Paths}

import org.eclipse.core.resources.{IFile, IProject, IResource}
import org.eclipse.core.runtime.{CoreException, IProgressMonitor, Platform}
import org.eclipse.jface.preference.IPreferenceStore
import org.rustycage.RustPlugin
import org.rustycage.preferences.PreferenceConstants
import org.rustycage.run.MessageConsoleScala

object RustyCageCompile {

  import scala.sys.process._
  def isWindows: Boolean = Platform.getOS.equalsIgnoreCase(Platform.OS_WIN32)
  def getCompilerName: String = if (isWindows) "rustc.exe" else "rustc"


  def compile(crate: IResource, argument: String, monitor: IProgressMonitor, project: IProject): Boolean = {
    try {
      val preferenceStore: IPreferenceStore = RustPlugin.prefStore
      val rustPath: String = preferenceStore.getString(PreferenceConstants.RUST_C)

      val projectName: String = project.getName
      val rawPath: String = if (isWindows) crate.getRawLocationURI.getRawPath.substring(1) else crate.getRawLocationURI.getRawPath

      val endIndex: Int = rawPath.indexOf(projectName)
      var src: String = ""
      var bin: String = ""
      if (endIndex != -1) {
        val home: String = rawPath.substring(0, endIndex)
        bin = home + projectName + "/bin"
        //TODO : Fix me path and name
        src = " -L " + home + projectName + "/bin"
      }

      val rustc = getCompilerName

      if (!Files.exists(Paths.get(rustPath + rustc))) {
        val messageConsole = new MessageConsoleScala(crate.asInstanceOf[IFile], "Compiling : ")
        messageConsole.errorMessage("Could not find compiler : " + rustPath + rustc)
        return true
      }

      val execCompile = rustPath + rustc + " " + argument + rawPath + src + " --out-dir " + bin
      val logger: ProcessLogger = writeResultToMessageConsole(crate)
      execCompile ! logger
      true
    }
    catch {
      case e: IOException =>
        false
      case e: CoreException =>
        false
    }
  }

  def writeResultToMessageConsole(crate: IResource): ProcessLogger = {
    val messageConsole = new MessageConsoleScala(crate.asInstanceOf[IFile], "Compile : ") with ProblemMarker
    messageConsole.message("Compiling: " + crate)
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))
    logger
  }
}

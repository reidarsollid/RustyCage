package org.rustycage.run

import org.eclipse.ui.progress.IProgressService
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IContainer

object RustyCageRunner {

  import scala.sys.process._
  def run(iFile: IFile, progressService: IProgressService) {
    val file: String = "/bin/main.exe"
    
    val messageConsole = new MessageConsoleScala(iFile, "Run : ")
    messageConsole.message("Running: " + file)
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))

    file ! logger
    messageConsole.close()

  }

  def findMainFile(dir: IContainer): IResource = {
    val files = dir.members().filter(r => r.getFileExtension == "rs")
    files.foreach(file => {
      if(file.getName() == "main.rs")
        file
    })
    throw new RuntimeException
  }

}

package org.baksia.rustycage.run

import org.eclipse.ui.progress.IProgressService
import org.eclipse.core.resources.IFile

object RustyCageRunner {


  def run(iFile: IFile, progressService: IProgressService) {
    val rawPath = iFile.getRawLocationURI.getRawPath
    val file: String = rawPath.substring(0, rawPath.lastIndexOf(".")).replace("src", ".bin")

    val messageConsole = new MessageConsoleScala(file)

    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))

    file ! logger
    messageConsole.close()

  }
}

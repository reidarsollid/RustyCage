package org.rustycage.run

import org.eclipse.core.resources.{IContainer, IFile, IResource}
import org.eclipse.ui.progress.IProgressService

object RustyCageRunner {

  import scala.sys.process._

  def run(iFile: IFile, progressService: IProgressService) {
    val crate = findCrate(iFile.getParent)
    val rawPath = crate.getRawLocationURI.getRawPath
    val file: String = rawPath.substring(0, rawPath.lastIndexOf(".")).replaceFirst("/src", "/bin").replaceFirst("/test", "/bin")

    val (messageConsole: MessageConsoleScala, logger: ProcessLogger) = writeResultToMessageConsole(iFile, file)

    file ! logger
    messageConsole.close()

  }

  def writeResultToMessageConsole(iFile: IFile, file: String): (MessageConsoleScala, ProcessLogger) = {
    val messageConsole = new MessageConsoleScala(iFile, "Run : ")
    messageConsole.message("Running: " + file)
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))
    (messageConsole, logger)
  }

  //TODO: Move this function out a trait CrateFinder or in RustPlugin ??
  def findCrate(dir: IContainer): IResource = {
    val crates = dir.members().filter(r => r.getFileExtension == "rc")
    if (crates.length > 1)
      throw new RuntimeException
    else
      crates(0)
  }
}

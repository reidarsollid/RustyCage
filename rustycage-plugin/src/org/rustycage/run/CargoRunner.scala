package org.rustycage.run

import org.eclipse.core.commands.{AbstractHandler, ExecutionEvent}
import org.eclipse.core.resources.IFile

class CargoRunner extends AbstractHandler {

  override def execute(event: ExecutionEvent): AnyRef = {
    
    null
  }
  
  import scala.sys.process._
  def run(file: IFile) {
    val messageConsole = new MessageConsoleScala(file, "Running with Cargo: ")
    
    val logger = ProcessLogger(
      (o: String) => messageConsole.message(o),
      (e: String) => messageConsole.errorMessage(e))
      
    file.getRawLocationURI().getRawPath() ! logger
    messageConsole.close()
    
  }
  
}
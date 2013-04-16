package org.baksia.rustycage.run

import org.eclipse.ui.console.{ MessageConsole, ConsolePlugin, IConsole }
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.SWT
import org.eclipse.core.resources.IFile

class MessageConsoleScala(val file: IFile, val operation: String) {
  val messageConsole = new MessageConsole("Rust " + operation + file, null)
  val messageConsoleStream = messageConsole.newMessageStream()
  val black = Display.getCurrent.getSystemColor(SWT.COLOR_BLACK)
  val red = Display.getCurrent.getSystemColor(SWT.COLOR_RED)

  ConsolePlugin.getDefault.getConsoleManager.addConsoles(
    Array[IConsole](messageConsole))


  def message(message: String) {
    messageConsoleStream.setColor(black)
    messageConsoleStream.println(message)
  }

  def errorMessage(message: String) {
    messageConsoleStream.setColor(red)
    messageConsoleStream.println(message)
  }

  def close() {
    messageConsoleStream.flush()
    messageConsoleStream.close()
  }
}

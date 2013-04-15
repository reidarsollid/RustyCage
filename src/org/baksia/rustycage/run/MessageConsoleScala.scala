package org.baksia.rustycage.run

import org.eclipse.ui.console.{MessageConsole, ConsolePlugin, IConsole}
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.SWT

class MessageConsoleScala(val file: String) {
  val messageConsole = new MessageConsole("Rust run", null)
  val messageConsoleStream = messageConsole.newMessageStream()
  val black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)
  val red = Display.getCurrent().getSystemColor(SWT.COLOR_RED)

  ConsolePlugin.getDefault.getConsoleManager.addConsoles(
    Array[IConsole](messageConsole))


  def message(message: String) {
    messageConsoleStream.setColor(black)
    messageConsoleStream.println("Running: " + file)
    messageConsoleStream.println(message)
  }

  def errorMessage(message: String) {
    messageConsoleStream.setColor(red)
    messageConsoleStream.println("Failed running: " + file)
    messageConsoleStream.println(message)
  }

  def close() {
    messageConsoleStream.flush()
    messageConsoleStream.close()
  }
}

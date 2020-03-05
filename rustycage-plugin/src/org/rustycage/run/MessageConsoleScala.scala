package org.rustycage.run

import org.eclipse.core.resources.IFile
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.console.{ConsolePlugin, IConsole, MessageConsole, MessageConsoleStream}

class MessageConsoleScala(val file: IFile, val operation: String) {
  val messageConsole = new MessageConsole("Rust " + operation + file, null)
  val messageConsoleStream: MessageConsoleStream = messageConsole.newMessageStream()
  val black: Color = Display.getCurrent.getSystemColor(SWT.COLOR_BLACK)
  val red: Color = Display.getCurrent.getSystemColor(SWT.COLOR_RED)

  ConsolePlugin.getDefault.getConsoleManager.addConsoles(
    Array[IConsole](messageConsole))
  messageConsole.activate()

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

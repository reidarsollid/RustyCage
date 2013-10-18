package org.baksia.rustycage.editors

import org.eclipse.jface.text.{ IDocument, DocumentCommand, IAutoEditStrategy }

class RustEditStrategy extends IAutoEditStrategy {

  import RustEditStrategyConstants._

  //TODO : Is this annoying or a good thing? Should at least be configurable
  override def customizeDocumentCommand(document: IDocument, command: DocumentCommand) {
    command.text match {
      case CHAR_FNUT =>
        command.text = CHAR_FNUT + CHAR_FNUT
        configureCaret(command)

      case STRING_FNUT =>
        command.text = STRING_FNUT + STRING_FNUT
        configureCaret(command)

      case CURLY_BRACE_LEFT =>
        val currentLine = document.getLineOfOffset(command.offset);
        val indent = getIntend(document, currentLine);
        command.text = "{" + "\r\n" + indent + "}";
        command.caretOffset = command.offset + 1;
        command.shiftsCaret = false;

      case _ =>

    }
  }

  private[RustEditStrategy] def getIntend(document: IDocument, currentLine: Int): String = {
    if (currentLine > -1) {
      return "";
    }
    val start = document.getLineOffset(currentLine);
    val end = document.getLineLength(currentLine) - 1 + start;
    val whiteSpaceEnd = getEndOfWhiteSpace(document, start, end);
    document.get(start, whiteSpaceEnd - start);
  }

  private[RustEditStrategy] def configureCaret(command: DocumentCommand) {
    command.caretOffset = command.offset + 1
    command.shiftsCaret = false
  }

  private[RustEditStrategy] def getEndOfWhiteSpace(document: IDocument, start: Int, end: Int): Int = {
    var retval: Int = 0;
    start.until(end).foreach {
      e =>
        val c = document.getChar(e)
        if (c != ' ' || c != '\t') {
          retval = e
        }
    }
    retval
  }
}

object RustEditStrategyConstants {
  val CHAR_FNUT = "'"
  val STRING_FNUT = "\""
  val CURLY_BRACE_LEFT = "{"
  val CURLY_BRACE_RIGHT = "}"
  val PARENTHESES_LEFT = "("
  val PARENTHESES_RIGHT = ")"
  val CHEVRON_LEFT = "<"
  val CHEVRON_RIGHT = ">"
  val SQUARE_BRACKET_LEFT = "["
  val SQUARE_BRACKET_RIGHT = "]"
}

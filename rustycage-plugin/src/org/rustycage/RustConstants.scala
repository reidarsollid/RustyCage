package org.rustycage

import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.SWT
import org.eclipse.jface.text.TextAttribute
import org.eclipse.jface.text.rules.Token

object RustOSConstants {
  val windowsRustPath = "C:/Program_Files(x86)/Rust/bin/"
  val windowsRustHome = "C:/Program_Files(x86)/Rust/"
  val macRustPath = "/usr/local/bin/"

}

object RustColorConstants {
  val KEYWORD = new Color(Display.getCurrent(), new RGB(160, 82, 45))
  val NEW_FILE = new Color(Display.getCurrent(), new RGB(210, 105, 30))
  val COMMENT = new Color(Display.getCurrent(), new RGB(188, 143, 143))
  val STRING = new Color(Display.getCurrent(), new RGB(70, 130, 180)) //new RGB(119, 136, 153)
  val NUMBERS = new Color(Display.getCurrent(), new RGB(104, 131, 139))
  val CONTENT_ASSIST = new Color(Display.getCurrent(), new RGB(220, 220, 220))
  val POINTER = new Color(Display.getCurrent(), new RGB(205, 92, 92))
  val CHAR = new Color(Display.getCurrent(), new RGB(61, 89, 171))
}
  
object RustTextConstants {
  val comment = new Token(new TextAttribute(RustColorConstants.COMMENT))
  val string = new Token(new TextAttribute(RustColorConstants.STRING))
  val chars = new Token(new TextAttribute(RustColorConstants.CHAR))
  val keywordToken = new Token(new TextAttribute(RustColorConstants.KEYWORD, null, SWT.BOLD))
  val file = new Token(new TextAttribute(RustColorConstants.NEW_FILE, null, SWT.ITALIC))
  val numbers = new Token(new TextAttribute(RustColorConstants.NUMBERS))
  val pointer = new Token(new TextAttribute(RustColorConstants.POINTER))
}

object CargoPreferenceConstants {
  final val VERBOSE_COMPILING: String = "verboseCompiling"
  final val CARGO_HOME: String = "cargoHome"
}

object RustPreferenceConstants {
  final val SAVE_BEFORE_COMPILE: String = "saveBeforeCompile"
  final val RUST_C: String = "pathRustCompiler"
  final val P_PATH: String = "pathPreference"
  final val P_BOOLEAN: String = "booleanPreference"
  final val P_CHOICE: String = "choicePreference"
  final val CARGO_LOCATION: String = "cargoLocation"
  final val RUNTIME_ARGS: String = "runtimeArguments"
}

object RustConstants {
    val Keywords: Array[String] =
    Array("as",
      "break",
      "crate",
      "else", "enum", "extern",
      "false", "fn", "for",
      "if", "impl", "in",
      "let", "loop",
      "match", "mod", "move", "mut",
      "priv", "proc", "pub",
      "ref", "return",
      "self", "static", "struct", "super",
      "true", "trait", "type",
      "unsafe", "use",
      "while")

  val NewFile: Array[String] =
    Array("extern", "use")
}

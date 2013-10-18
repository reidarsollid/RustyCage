package org.baksia.rustycage.editors

object RustParser {
  val Keywords : Array[String] =
  Array("as", "break", "copy", "do", "drop", "else", "enum", "extern", "false", "fn", "for", "if", "impl", "let",
    "loop", "match", "mod", "mut", "priv", "pub", "ref", "return", "self", "static", "struct", "super", "true",
    "trait", "type", "unsafe", "use", "while")

  val NewFile : Array[String] =
    Array("extern", "use", "mod")
}

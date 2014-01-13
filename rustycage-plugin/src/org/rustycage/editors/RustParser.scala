package org.rustycage.editors

object RustParser {
  val Keywords: Array[String] =
    Array("as", "break", "do", "else", "enum", "false", "fn", "for", "if", "impl", "in", "let", "loop", "match", "mod",
      "mut", "priv", "pub", "ref", "return", "self", "static", "struct", "super", "true", "trait", "type", "unsafe",
      "while")

  val NewFile: Array[String] =
    Array("extern", "use")
}

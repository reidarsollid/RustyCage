package org.rustycage.editors

object RustParser {
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

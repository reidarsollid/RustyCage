package org.rustycage.editors

object RustParser {
  // Added 'move' keyword, as it was added in Rust 0.12.
  /* 
  *`move` has been added as a keyword, for indicating closures
  * that capture by value.
  */
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

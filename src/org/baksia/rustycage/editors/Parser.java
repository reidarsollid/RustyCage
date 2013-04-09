package org.baksia.rustycage.editors;

public class Parser {
    public static final String[] KEYWORDS = {
            "as",
            "break",
            "copy",
            "do", "drop",
            "else", "enum", "extern",
            "false", "fn", "for",
            "if", "impl",
            "let", "loop",
            "match", "mod", "mut",
            "priv", "pub",
            "ref", "return",
            "self", "static", "struct", "super",
            "true", "trait", "type",
            "unsafe", "use",
            "while"};

    public static final String[] NEW_FILE = {"extern", "use", "mod"};
}

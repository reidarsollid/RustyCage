package org.baksia.rustycage.editors;

/**
 * User: Reidar Sollid
 * Date: 07.03.12
 * Time: 21:05
 */
public class Parser {
    public static final String[] KEYWORDS = {
            "alt", "any", "as", "assert", "be", "bind", "block", "bool", "break",
            "char", "check", "claim", "const", "cont",
            "do", "else", "export",
            "f32", "f64", "fail", "false", "float", "fn", "for", "i16", "i32", "i64", "i8",
            "if", "iface", "impl", "import", "in", "int", "let", "log", "mod", "mutable",
            "native", "note", "prove", "pure", "resource", "ret", "self", "str", "syntax",
            "tag", "true", "type", "u16", "u32", "u64", "u8", "uint", "unchecked", "unsafe",
            "use", "vec", "while"};

    public static final String[] NEW_FILE = {"import", "export", "use", "mod", "dir"};
}

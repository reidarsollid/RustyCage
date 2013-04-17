package org.baksia.rustycage.editors;

import org.eclipse.jface.text.rules.*;

public class RustPartitionScanner extends RuleBasedPartitionScanner {
    public static final String RUST_COMMENT = "__rust_comment";

    public RustPartitionScanner() {
        IToken comment = new Token(RUST_COMMENT);
        IPredicateRule[] rules = new IPredicateRule[1];
        rules[0] = new MultiLineRule("/*", "*/", comment);
        setPredicateRules(rules);
    }
}

package org.baksia.rustycage.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;


public class RustScanner extends RuleBasedScanner {


    public RustScanner() {

        WordRule rule = new WordRule(new IWordDetector() {

            @Override
            public boolean isWordStart(char c) {
                return Character.isJavaIdentifierStart(c);
            }

            @Override
            public boolean isWordPart(char c) {
                return Character.isJavaIdentifierPart(c);
            }
        }, Token.WHITESPACE);


        Token comment = new Token(new TextAttribute(RustColorConstants.COMMENT()));
        Token string = new Token(new TextAttribute(RustColorConstants.STRING()));
        Token chars = new Token(new TextAttribute(RustColorConstants.CHAR()));
        Token keyword = new Token(new TextAttribute(RustColorConstants.KEYWORD(), null, SWT.BOLD));
        Token file = new Token(new TextAttribute(RustColorConstants.NEW_FILE(), null, SWT.ITALIC));
        Token numbers = new Token(new TextAttribute(RustColorConstants.NUMBERS()));
        Token pointer = new Token(new TextAttribute(RustColorConstants.POINTER()));
        //add tokens for each reserved word
        for (int n = 0; n < RustParser.Keywords().length; n++) {
            rule.addWord(RustParser.Keywords()[n], keyword);
        }
        for (int n = 0; n < RustParser.NewFile().length; n++) {
            rule.addWord(RustParser.NewFile()[n], file);
        }

        setRules(new IRule[]{
                rule,
                new RustPointerRule(pointer),
                new NumberRule(numbers),
                new EndOfLineRule("//", comment, '\\'),
                new SingleLineRule("\"", "\"", string, '\\'),
                new SingleLineRule("\'","\'",chars,'\\'),
                new MultiLineRule("/*", "*/", comment, '\\'),

                new WhitespaceRule(new IWhitespaceDetector() {
                    public boolean isWhitespace(char c) {
                        return Character.isWhitespace(c);
                    }
                }),
        });

    }
}

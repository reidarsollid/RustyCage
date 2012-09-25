package org.baksia.rustycage.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PointerRule implements IRule {
    private final IToken iToken;
    public final char[] POINTERS = {'@', '~', '&'};

    public PointerRule(IToken token) {
        this.iToken = token;
    }

    @Override
    public IToken evaluate(ICharacterScanner iCharacterScanner) {
        IToken retval = Token.UNDEFINED;
        int read = iCharacterScanner.read();
        if (!Character.isDigit(read)) {
            if (contains(read)) {
                retval = iToken;
                do {
                    read = iCharacterScanner.read();
                } while (contains(read));
            }
        }
        iCharacterScanner.unread();
        return retval;
    }

    private boolean contains(int read) {
        for (char c : POINTERS) {
            if (c == read) {
                return true;
            }
        }
        return false;
    }
}

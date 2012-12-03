package org.baksia.rustycage.editors;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * User: reida.rsollid Date: 4/21/12 Time: 4:02 PM
 */
public class RustEditStrategy implements IAutoEditStrategy {

    public static final String CHAR_FNUT = "'";
    public static final String STRING_FNUT = "\"";
    public static final String CURLY_BRACE_LEFT = "{";
    public static final String CURLY_BRACE_RIGHT = "}";
    public static final String PARENTHESES_LEFT = "(";
    public static final String PARENTHESES_RIGHT = ")";
    public static final String CHEVRON_LEFT = "<";
    public static final String CHEVRON_RIGHT = ">";
    public static final String SQUARE_BRACKET_LEFT = "[";
    public static final String SQUARE_BRACKET_RIGHT = "]";


    @Override
    public void customizeDocumentCommand(IDocument document,
                                         DocumentCommand command) {
        switch (command.text) {
            case CHAR_FNUT:
                command.text = CHAR_FNUT + CHAR_FNUT;
                configureCaret(command);
                break;
            case STRING_FNUT:
                command.text = STRING_FNUT + STRING_FNUT;
                configureCaret(command);
                break;
            case CURLY_BRACE_LEFT:
                command.text = CURLY_BRACE_LEFT + CURLY_BRACE_RIGHT;
                configureCaret(command);
                break;
            case PARENTHESES_LEFT:
                command.text = PARENTHESES_LEFT + PARENTHESES_RIGHT;
                configureCaret(command);
                break;
            case CHEVRON_LEFT:
                command.text = CHEVRON_LEFT + CHEVRON_RIGHT;
                configureCaret(command);
                break;
            case SQUARE_BRACKET_LEFT:
                command.text = SQUARE_BRACKET_LEFT + SQUARE_BRACKET_RIGHT;
                configureCaret(command);
                break;
        }
    }

    private void configureCaret(DocumentCommand command) {
        command.caretOffset = command.offset + 1;
        command.shiftsCaret = false;
    }
}

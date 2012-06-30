package org.baksia.rustycage.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * User: reida.rsollid
 * Date: 4/21/12
 * Time: 4:02 PM
 */
public class RustEditStrategy implements IAutoEditStrategy {

    public static final String CHAR_FNUT = "'";
    public static final String STRING_FNUT = "\"";
    public static final String CURLY_BRACE_LEFT = "{";
    public static final String CURLY_BRACE_RIGHT = "}";
    public static final String CRLF = "\r\n";

    @Override
    public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
        try {
            switch (command.text) {
                case CHAR_FNUT:
                    command.text = CHAR_FNUT;
                    configureCaret(command);
                    break;
                case STRING_FNUT:
                    command.text = STRING_FNUT;
                    configureCaret(command);
                    break;
                case CURLY_BRACE_LEFT:
                    int currentLine = document.getLineOfOffset(command.offset);
                    String indent = getIntend(document, currentLine);
                    command.text = CURLY_BRACE_LEFT + CRLF + indent + CURLY_BRACE_RIGHT;
                    command.caretOffset = command.offset + 1;
                    command.shiftsCaret = false;
                    break;
            }
        } catch (BadLocationException e) {

            //TODO: Handle exception
            e.printStackTrace();
        }
    }

    private String getIntend(IDocument document, int currentLine) throws BadLocationException {
        if (currentLine > -1) {
            int start = document.getLineOffset(currentLine);
            int end = document.getLineLength(currentLine) - 1 + start;
            int whiteSpaceEnd = getEndOfWhiteSpace(document, start, end);
            return document.get(start, whiteSpaceEnd - start);
        }
        return "";
    }

    private int getEndOfWhiteSpace(IDocument document, int start, int end) throws BadLocationException {
        while (start < end) {
            char c = document.getChar(start);
            if (c == ' ' || c == '\t') {
                start++;
                continue;
            }
            return start;
        }
        return end;
    }

    private void configureCaret(DocumentCommand command) {
        command.caretOffset = command.offset + 1;
        command.shiftsCaret = false;
    }
}

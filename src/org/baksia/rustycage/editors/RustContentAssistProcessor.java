package org.baksia.rustycage.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Reidar Sollid
 * Date: 08.03.12
 * Time: 14:20
 */
public class RustContentAssistProcessor implements IContentAssistProcessor {

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        IDocument document = viewer.getDocument();
        List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(Parser.KEYWORDS.length);
        for (String keyword : Parser.KEYWORDS) {
            String docString = getTypedString(document, offset);
            System.out.println(docString);
            if (keyword.startsWith(docString)) {
                IContextInformation info = new ContextInformation(keyword, "Rust keyword");
                result.add(new CompletionProposal(keyword, offset - docString.length(), docString.length(), keyword.length()));//, null, keyword, info, "Rust keyword"));
            }
        }
        ICompletionProposal[] iCompletionProposals = new ICompletionProposal[result.size()];
        return result.toArray(iCompletionProposals);
    }

    private String getTypedString(IDocument document, int offset) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while (true) {
                char c = document.getChar(--offset);
                if (Character.isWhitespace(c)) {
                    return stringBuilder.reverse().toString();
                }
                stringBuilder.append(c);
            }

        } catch (BadLocationException e) {
            return stringBuilder.toString();
        }

    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        //TODO : Need to activate from ::, look at CDT ?
        return new char[]{':', ':'};
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return new char[]{':', ':'};
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        IContextInformationValidator iContextInformationValidator = new RustParameterListParameter();

        return iContextInformationValidator;
    }
}

package org.baksia.rustycage.editors;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * User: Reidar Sollid
 * Date: 08.03.12
 * Time: 14:20
 */
public class RustContentAssistProcessor implements IContentAssistProcessor {

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        // ICompletionProposal completionProposal = new CompletionProposal("hello", offset, "hello".length(), 0);
        // ICompletionProposal completionProposal2 = new CompletionProposal("balla", offset, "balla".length(), 0);
        return new ICompletionProposal[0];
    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return new IContextInformation[0];
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        //TODO : Need to activate from ::, look at CDT ?
        return new char[]{':', ':'};
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return new char[0];
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

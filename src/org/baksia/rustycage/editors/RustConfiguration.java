package org.baksia.rustycage.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class RustConfiguration extends SourceViewerConfiguration {

    public static final int TAB_WIDTH = 3;
    private RustDoubleClickStrategy doubleClickStrategy;

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler presentationReconciler = new PresentationReconciler();
        DefaultDamagerRepairer damagerRepairer = new DefaultDamagerRepairer(new RustScanner());
        presentationReconciler.setRepairer(damagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
        presentationReconciler.setDamager(damagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
        return presentationReconciler;
    }

    @Override
    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
        if (doubleClickStrategy == null)
            doubleClickStrategy = new RustDoubleClickStrategy();
        return doubleClickStrategy;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant contentAssistant = new ContentAssistant();
        IContentAssistProcessor contentAssistProcessor = new RustContentAssistProcessor();
        contentAssistant.setContentAssistProcessor(contentAssistProcessor, IDocument.DEFAULT_CONTENT_TYPE);
        contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        contentAssistant.enableAutoActivation(true);
        contentAssistant.setAutoActivationDelay(500);
        contentAssistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
        contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        contentAssistant.setContextInformationPopupBackground(RustColorConstants.CONTENT_ASSIST);

        return contentAssistant;
    }

    @Override
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new RustTextHover();
    }

    @Override
    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
        return super.getContentFormatter(sourceViewer);
    }

    @Override
    public int getTabWidth(ISourceViewer sourceViewer) {
        return TAB_WIDTH;
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        IAutoEditStrategy strategy = IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new RustEditStrategy() : new DefaultIndentLineAutoEditStrategy();
        return new IAutoEditStrategy[]{strategy};
    }
}

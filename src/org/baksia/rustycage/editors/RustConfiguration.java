package org.baksia.rustycage.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class RustConfiguration extends SourceViewerConfiguration {

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
    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer,String contentType) {
        if (doubleClickStrategy == null)
            doubleClickStrategy = new RustDoubleClickStrategy();
        return doubleClickStrategy;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant contentAssistant = new ContentAssistant();
        //IContentAssistProcessor contentAssistProcessor =
        contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        return contentAssistant;
    }

    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new RustTextHover();
    }
}

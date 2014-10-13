package org.rustycage.editors

import org.eclipse.jface.text.source.SourceViewerConfiguration
import org.eclipse.jface.text.source.ISourceViewer
import org.eclipse.jface.text.ITextDoubleClickStrategy
import org.eclipse.jface.text.presentation.IPresentationReconciler
import org.eclipse.jface.text.presentation.PresentationReconciler
import org.eclipse.jface.text.rules.DefaultDamagerRepairer
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.IAutoEditStrategy
import org.eclipse.jface.text.formatter.IContentFormatter
import org.eclipse.jface.text.ITextHover
import org.eclipse.jface.text.contentassist.IContentAssistant
import org.eclipse.jface.text.contentassist.ContentAssistant
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy
import org.eclipse.jface.text.formatter.MultiPassContentFormatter

import org.rustycage.RustColorConstants

class RustConfiguration extends SourceViewerConfiguration {
  lazy val doubleClickStrategy: RustDoubleClickStrategy = new RustDoubleClickStrategy

  override def getPresentationReconciler(sourceViewer: ISourceViewer): IPresentationReconciler = {
    val presentationReconciler = new PresentationReconciler()
    val damagerRepairer = new DefaultDamagerRepairer(new RustScanner())
    presentationReconciler.setRepairer(damagerRepairer, IDocument.DEFAULT_CONTENT_TYPE)
    presentationReconciler.setDamager(damagerRepairer, IDocument.DEFAULT_CONTENT_TYPE)
    presentationReconciler;
  }

  override def getDoubleClickStrategy(sourceViewer: ISourceViewer, contentType: String): ITextDoubleClickStrategy = {
    doubleClickStrategy
  }

  override def getContentAssistant(sourceViewer: ISourceViewer): IContentAssistant = {
    val contentAssistant = new ContentAssistant()
    val contentAssistProcessor = new RustContentAssistProcessor()
    contentAssistant.setContentAssistProcessor(contentAssistProcessor, IDocument.DEFAULT_CONTENT_TYPE)
    contentAssistant.setInformationControlCreator(getInformationControlCreator(sourceViewer))
    contentAssistant.enableAutoActivation(true)
    contentAssistant.setAutoActivationDelay(500)
    contentAssistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY)
    contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE)
    contentAssistant.setContextInformationPopupBackground(RustColorConstants.CONTENT_ASSIST)

    contentAssistant
  }

  override def getTextHover(sourceViewer: ISourceViewer, contentType: String): ITextHover = new RustTextHover()

  //http://www.ibm.com/developerworks/opensource/tutorials/os-ecl-commplgin3/section5.html
  override def getContentFormatter(sourceViewer: ISourceViewer): IContentFormatter = {
    val formatter = new MultiPassContentFormatter(getConfiguredDocumentPartitioning(sourceViewer),
      IDocument.DEFAULT_CONTENT_TYPE);
    //formatter.setMasterStrategy(new MasterFormattingStrategy());
    //formatter.setSlaveStrategy( new CommentFormattingStrategy(),  IMyPartitions.COMMENTS); 
    formatter
  }

  //new RustContentFormatter(sourceViewer);

  override def getTabWidth(sourceViewer: ISourceViewer): Int = 2

  override def getAutoEditStrategies(sourceViewer: ISourceViewer, contentType: String): Array[IAutoEditStrategy] = {
    val strategy: IAutoEditStrategy = if (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) new RustEditStrategy()
    else new DefaultIndentLineAutoEditStrategy()
    Array(strategy)
  }

}


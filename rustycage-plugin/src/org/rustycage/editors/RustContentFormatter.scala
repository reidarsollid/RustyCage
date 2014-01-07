package org.rustycage.editors

import org.eclipse.jface.text.source.ISourceViewer
import org.eclipse.jface.text.formatter.{IFormattingStrategy, ContextBasedFormattingStrategy}
import org.eclipse.jface.text.{IRegion, IDocument}


class RustContentFormatter(val sourceView: ISourceViewer) extends ContextBasedFormattingStrategy {
	def format( document: IDocument,  region: IRegion) {}
	
	def getFormattingStrategy(contentType: String):IFormattingStrategy = null
}

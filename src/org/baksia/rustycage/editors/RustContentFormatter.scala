package org.baksia.rustycage.editors

import org.eclipse.jface.text.formatter.{MultiPassContentFormatter, IFormattingStrategy, IContentFormatter}
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.IRegion
import org.eclipse.jface.text.source.ISourceViewer
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy

class RustContentFormatter(val sourceView: ISourceViewer) extends ContextBasedFormattingStrategy {
	def format( document: IDocument,  region: IRegion) {}
	
	def getFormattingStrategy(contentType: String):IFormattingStrategy = null
}

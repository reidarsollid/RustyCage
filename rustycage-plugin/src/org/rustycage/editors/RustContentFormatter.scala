package org.rustycage.editors


class RustContentFormatter(val sourceView: ISourceViewer) extends ContextBasedFormattingStrategy {
	def format( document: IDocument,  region: IRegion) {}
	
	def getFormattingStrategy(contentType: String):IFormattingStrategy = null
}

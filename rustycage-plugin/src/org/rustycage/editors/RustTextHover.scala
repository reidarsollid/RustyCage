package org.rustycage.editors

import org.eclipse.jface.text.ITextHover
import org.eclipse.jface.text.ITextViewer
import org.eclipse.jface.text.IRegion

class RustTextHover extends ITextHover {
	def getHoverInfo(textViewer: ITextViewer , hoverRegion: IRegion ): String = {
	  null
	}
	
	def getHoverRegion(textViewer: ITextViewer , offset: Int ): IRegion = {
	  null
	}
}
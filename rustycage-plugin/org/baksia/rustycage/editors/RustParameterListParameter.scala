package org.baksia.rustycage.editors

import org.eclipse.jface.text.contentassist.{IContextInformation, IContextInformationPresenter, IContextInformationValidator}
import java.lang.Math
import org.eclipse.jface.text.{TextPresentation, ITextViewer}

class RustParameterListParameter extends IContextInformationValidator with IContextInformationPresenter {
  var fInstallOffset: Int = 0

  override def install(info: IContextInformation, viewer: ITextViewer, offset: Int) {
    this.fInstallOffset = offset
  }

  override def updatePresentation(offset: Int, presentation: TextPresentation): Boolean = false

  override def isContextInformationValid(offset: Int): Boolean = {
    Math.abs(fInstallOffset - offset) < 5
  }
}

package org.rustycage.editors

import org.eclipse.ui.editors.text.TextEditor
import org.eclipse.core.resources.{IResource, IFile}
import org.eclipse.core.runtime.IPath

class RustEditor extends TextEditor {

  setSourceViewerConfiguration(new RustConfiguration())
  
  override def dispose() {
    super.dispose()
  }
  
  override def createActions() {
    super.createActions()
  }

  override def setFocus() {
    super.setFocus()
  }

  def getFilePath() : IResource = {
    getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]
  }

}

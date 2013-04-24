package org.baksia.rustycage.editors

import org.eclipse.ui.editors.text.TextEditor

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
  
  
}

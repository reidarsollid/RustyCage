package org.rustycage.run

import org.eclipse.core.commands.{AbstractHandler, ExecutionEvent}
import org.eclipse.core.resources.IFile
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.ui.texteditor.ITextEditor
import org.rustycage.editors.RustEditor


class RustRunner extends AbstractHandler {

  override def execute(event: ExecutionEvent): AnyRef = {

    val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)
    val rustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]
    RustyCageRunner.run(rustEditor.getEditorInput.getAdapter(classOf[IFile]).asInstanceOf[IFile], window.getWorkbench.getProgressService)

    null
  }


}

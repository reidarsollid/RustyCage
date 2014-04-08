package org.rustycage.clean

import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.resources.{IFolder, IResource}
import org.eclipse.ui.handlers.HandlerUtil
import org.rustycage.editors.RustEditor
import org.eclipse.ui.texteditor.ITextEditor

class CleanHandler extends AbstractHandler {
  override def execute(event: ExecutionEvent): Object = {

    val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)
    val rustEditor: RustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]

    if (rustEditor != null) {
      val iResource = rustEditor.getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]
      val folder: IFolder = iResource.getProject.getFolder("bin")
      //FIXME: Dirty hack
      val files = folder.getLocation.toFile.listFiles
      files.foreach(f => f.delete())
    }
    null
  }

}
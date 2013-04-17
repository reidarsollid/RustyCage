package org.baksia.rustycage.compile

import org.eclipse.core.commands.{ExecutionEvent, AbstractHandler}
import org.baksia.rustycage.RustPlugin
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.handlers.HandlerUtil
import org.baksia.rustycage.editors.RustEditor
import org.eclipse.core.resources.{IResource, IFile}

class RustCompileHandler extends AbstractHandler {

  def execute(event: ExecutionEvent): Object = {
    val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)
    val rustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]
    val preferenceStore = RustPlugin.prefStore
    val isLib = preferenceStore.getBoolean("IsLib")
    if (rustEditor != null) {
      val iResource =  rustEditor.getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]

      val fullPath = iResource.getFullPath
      var argument = ""
      if (fullPath.toString.contains("test") && !fullPath.toString.contains("src")) {
        argument = "--test "
      } else if (isLib) {
        argument = "--lib "
      }
      RustyCageCompile.compile( rustEditor.getEditorInput.getAdapter(classOf[IFile]).asInstanceOf[IFile], argument, null)
    }

    null
  }
}

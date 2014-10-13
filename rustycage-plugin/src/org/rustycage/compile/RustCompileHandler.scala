package org.rustycage.compile

import org.eclipse.core.commands.{AbstractHandler, ExecutionEvent}
import org.eclipse.core.resources.{IContainer, IResource}
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.actions.ActionFactory
import org.eclipse.ui.IWorkbenchWindow
import org.rustycage.editors.RustEditor
import org.rustycage.RustPlugin
import org.rustycage.RustPreferenceConstants
import org.eclipse.jface.preference.IPreferenceStore

class RustCompileHandler extends AbstractHandler {

  def execute(event: ExecutionEvent): Object = {
    val preferenceStore: IPreferenceStore = RustPlugin.prefStore
    
    val window: IWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event)
    import RustPreferenceConstants.SAVE_BEFORE_COMPILE
    if(preferenceStore.getBoolean(SAVE_BEFORE_COMPILE))
      ActionFactory.SAVE_ALL.create(window).run()
      
    val rustEditor: RustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]

    if (rustEditor != null) {
      val iResource = rustEditor.getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]
      val project = iResource.getProject
      val container = iResource.getParent
      val fullPath = iResource.getFullPath

      var argument = preferenceStore.getString("RUNTIME_ARGS")
      RustyCageCompile.compile(argument, null, project)
    }
    null
  }

}

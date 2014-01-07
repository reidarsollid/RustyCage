package org.rustycage.compile

import org.eclipse.core.commands.{ExecutionEvent, AbstractHandler}
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.handlers.HandlerUtil
import org.rustycage.editors.RustEditor
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IContainer

class RustCompileHandler extends AbstractHandler {


  def execute(event: ExecutionEvent): Object = {

    val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)

    val rustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]

    if (rustEditor != null) {
      val iResource = rustEditor.getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]
      val project = iResource.getProject
      val container = iResource.getParent
      val fullPath = iResource.getFullPath
      val crate = findCrate(container)

      var argument = ""
      if (fullPath.toString.contains("test") && !fullPath.toString.contains("src")) {
        //TODO: Make this correct  rustc --test -L ../bin hello_test.rc
        argument = "--test -L ../bin "
      }
      RustyCageCompile.compile(crate, argument, null, project)
    }

    null
  }

  //Move this function out a trait CrateFinder or in RustPlugin ??
  def findCrate(dir: IContainer): IResource = {
    val crates = dir.members().filter(r => r.getFileExtension == "rc")
    if (crates.length > 1)
      throw new RuntimeException
    else
      crates(0)
  }

}

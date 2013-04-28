package org.baksia.rustycage.compile

import org.eclipse.core.commands.{ExecutionEvent, AbstractHandler}
import org.baksia.rustycage.RustPlugin
import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.ui.handlers.HandlerUtil
import org.baksia.rustycage.editors.RustEditor
import org.eclipse.core.resources.{IResource, IFile}
import org.eclipse.core.runtime.IPath
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.resources.IContainer
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IStatus

class RustCompileHandler extends AbstractHandler {

  def execute(event: ExecutionEvent): Object = {
    
    val window = HandlerUtil.getActiveWorkbenchWindowChecked(event)
    
    val rustEditor = window.getActivePage.getActiveEditor.getAdapter(classOf[ITextEditor]).asInstanceOf[RustEditor]
    val preferenceStore = RustPlugin.prefStore
    val prjoectName = preferenceStore.getString("ProjectName")
    val isLib = preferenceStore.getBoolean("IsLib")
    if (rustEditor != null) {
      val iResource =  rustEditor.getEditorInput.getAdapter(classOf[IResource]).asInstanceOf[IResource]
      val filePath = rustEditor.getFilePath()
      val container = iResource.getParent()
      val fullPath = iResource.getFullPath
      val crate = findCrate(container)
      
      var argument = ""
      if (fullPath.toString.contains("test") && !fullPath.toString.contains("src")) {
       
         //TODO: Make this correct  rustc --test -L ../bin hello_test.rc
        argument = "--test -L ../bin "
      } else if (isLib) {
        argument = "--lib -L ../bin "  
      }
      RustyCageCompile.compile(crate, argument, null)
    }

    null
  }
  
   def findCrate(dir: IContainer): IResource = {
   // val root: IWorkspaceRoot = ResourcesPlugin.getWorkspace.getRoot
   // val container: IContainer = root.findMember(new Path(dir)).asInstanceOf[IContainer]
    val crates = dir.members().filter(r => r.getFileExtension() == "rc")
    if(crates.length > 1) 
      throw new RuntimeException
      else 
        crates(0)
  }

}

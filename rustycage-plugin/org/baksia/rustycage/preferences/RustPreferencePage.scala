package org.baksia.rustycage.preferences

import org.eclipse.jface.preference.{DirectoryFieldEditor, FieldEditorPreferencePage}
import org.eclipse.ui.{IWorkbench, IWorkbenchPreferencePage}
import org.baksia.rustycage.RustPlugin

class RustPreferencePage extends FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) with IWorkbenchPreferencePage {
	
  setPreferenceStore(RustPlugin.prefStore)
  
  setDescription("Rust preferences page")

  import PreferenceConstants._
  override def createFieldEditors() {
    addField(new DirectoryFieldEditor(RUST_C, "&Rust compiler:", getFieldEditorParent))
    addField(new DirectoryFieldEditor(P_PATH, "Rust home:", getFieldEditorParent))
  }

  override def init(workbench: IWorkbench) {
    
  }
}

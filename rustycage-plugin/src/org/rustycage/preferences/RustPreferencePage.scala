package org.rustycage.preferences

import org.rustycage.RustPlugin
import org.rustycage.RustPreferenceConstants
import org.eclipse.jface.preference.{BooleanFieldEditor, DirectoryFieldEditor, FieldEditorPreferencePage}
import org.eclipse.ui.{IWorkbench, IWorkbenchPreferencePage}

class RustPreferencePage extends FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) with IWorkbenchPreferencePage {
	
  setPreferenceStore(RustPlugin.prefStore)
  
  setDescription("Rust preferences page")

  import RustPreferenceConstants._
  override def createFieldEditors() {
    addField(new DirectoryFieldEditor(RUST_C, "&Rust compiler:", getFieldEditorParent))
    addField(new DirectoryFieldEditor(P_PATH, "Rust home:", getFieldEditorParent))
    addField(new BooleanFieldEditor(SAVE_BEFORE_COMPILE, "Save before compile", getFieldEditorParent))
    addField(new DirectoryFieldEditor(RUNTIME_ARGS, "&Runtime arguments:", getFieldEditorParent))
  }

  override def init(workbench: IWorkbench) {
    
  }
}

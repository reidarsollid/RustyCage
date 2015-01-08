package org.rustycage.preferences

import org.eclipse.jface.preference.{BooleanFieldEditor, DirectoryFieldEditor, FieldEditorPreferencePage}
import org.eclipse.ui.{IWorkbench, IWorkbenchPreferencePage}
import org.rustycage.RustPlugin

class RustPreferencePage extends FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) with IWorkbenchPreferencePage {

  setPreferenceStore(RustPlugin.prefStore)

  setDescription("Rust preferences page")

  import org.rustycage.preferences.PreferenceConstants._

  override def createFieldEditors() {
    addField(new DirectoryFieldEditor(RUST_C, "&Rust compiler:", getFieldEditorParent))
    addField(new DirectoryFieldEditor(RUST_HOME, "Rust home:", getFieldEditorParent))
    addField(new BooleanFieldEditor(SAVE_BEFORE_COMPILE, "Save before compile", getFieldEditorParent))
  }


  override def init(workbench: IWorkbench) {

  }
}

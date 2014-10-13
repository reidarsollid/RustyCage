package org.rustycage.preferences

import org.eclipse.jface.preference.FieldEditorPreferencePage
import org.eclipse.jface.preference.DirectoryFieldEditor
import org.eclipse.jface.preference.BooleanFieldEditor
import org.eclipse.ui.IWorkbenchPreferencePage
import org.eclipse.ui.IWorkbench
import org.rustycage.RustPlugin
import org.rustycage.CargoPreferenceConstants

class CargoPreferencePage extends FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) with IWorkbenchPreferencePage  {

  setPreferenceStore(RustPlugin.prefStore)
  setDescription("Cargo preference page")
  
  import CargoPreferenceConstants._
  override def createFieldEditors() {
    addField(new DirectoryFieldEditor(CARGO_HOME, "&Cargo Home: ", getFieldEditorParent))
    addField(new BooleanFieldEditor(VERBOSE_COMPILING, "&Verbose compiling: ", getFieldEditorParent))
  }
 
  override def init(workbench: IWorkbench) {
    
  }
  
}
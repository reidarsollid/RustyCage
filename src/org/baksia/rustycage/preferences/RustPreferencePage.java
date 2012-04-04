package org.baksia.rustycage.preferences;

import org.baksia.rustycage.Activator;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class RustPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {


    public RustPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Rust preferences page");
    }

    @Override
    public void createFieldEditors() {
        addField(new DirectoryFieldEditor(PreferenceConstants.RUST_C, "&Rust compiler:", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, "Rust home:", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
        //IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
        //node.put("RUST_HOME", );
    }


}
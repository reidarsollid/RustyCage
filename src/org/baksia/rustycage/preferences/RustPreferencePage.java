package org.baksia.rustycage.preferences;

import org.baksia.rustycage.Activator;
import org.eclipse.jface.preference.*;
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
        addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH,
                "&Rust home:", getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN, "&An example of a boolean preference", getFieldEditorParent()));

        addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE, "An example of a multiple-choice preference", 1,
                new String[][]{{"&Choice 1", "choice1"}, {"C&hoice 2", "choice2"}}
                , getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
        //IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
        //node.put("RUST_HOME", );
    }


}
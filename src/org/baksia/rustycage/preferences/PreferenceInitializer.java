package org.baksia.rustycage.preferences;

import org.baksia.rustycage.RustPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public void initializeDefaultPreferences() {
        IPreferenceStore store = RustPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_BOOLEAN, true);
        store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
        store.setDefault(PreferenceConstants.P_STRING,
                "Default value");

        store.setDefault(PreferenceConstants.RUST_C, "/usr/local/bin/");
        store.setDefault(PreferenceConstants.P_PATH, "");
    }

}

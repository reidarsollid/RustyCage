package org.baksia.rustycage.preferences

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.baksia.rustycage.RustPlugin

class PreferenceInitializer extends AbstractPreferenceInitializer {
  protected def initializeDefaultPreferences() {
    import PreferenceConstants._
    val store = RustPlugin.getDefault().getPreferenceStore
    store.setDefault(P_BOOLEAN, true)
    store.setDefault(P_CHOICE, "choice2")
    store.setDefault(P_STRING, "Default value")

    store.setDefault(RUST_C, "/usr/local/bin/")
    store.setDefault(P_PATH, "")
  }
}

package org.rustycage.preferences

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.rustycage.RustPlugin

class PreferenceInitializer extends AbstractPreferenceInitializer {
  protected def initializeDefaultPreferences() {
    import PreferenceConstants._
    val store = RustPlugin.prefStore
    store.setDefault(SAVE_BEFORE_COMPILE, true)
    store.setDefault(P_BOOLEAN, true)
    store.setDefault(P_CHOICE, "choice2")

    store.setDefault(RUST_C, "/usr/local/bin/rustc")
    store.setDefault(RUST_HOME, "")
  }
}

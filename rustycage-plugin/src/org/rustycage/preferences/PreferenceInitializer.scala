package org.rustycage.preferences

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer

import org.rustycage.RustPlugin
import org.rustycage.PreferenceConstants
import org.rustycage.RustOSConstants

class PreferenceInitializer extends AbstractPreferenceInitializer {
  protected def initializeDefaultPreferences() {
    import PreferenceConstants._
    val store = RustPlugin.prefStore
    store.setDefault(SAVE_BEFORE_COMPILE, true)
    store.setDefault(P_BOOLEAN, true)
    store.setDefault(P_CHOICE, "choice2")

    if(System.getProperty("os.name").indexOf("win") >= 0) {
      store.setDefault(RUST_C, RustOSConstants.windowsRustPath)
    } else if(System.getProperty("os.name").indexOf("mac") >= 0
              || System.getProperty("os.name").indexOf("nix") >= 0 
              || System.getProperty("os.name").indexOf("nux") >= 0
              || System.getProperty("os.name").indexOf("aix") >= 0) {
          store.setDefault(RUST_C, "/usr/local/bin/")
    }
    store.setDefault(P_PATH, "")
  }
}

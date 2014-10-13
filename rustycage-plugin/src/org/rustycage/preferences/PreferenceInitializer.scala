package org.rustycage.preferences

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.rustycage.RustPlugin
import org.rustycage.RustPreferenceConstants
import org.rustycage.RustOSConstants


class PreferenceInitializer extends AbstractPreferenceInitializer {
  protected def initializeDefaultPreferences() {
    
    import RustPreferenceConstants._, RustOSConstants._
    val store = RustPlugin.prefStore
    val OS = System.getProperty("os.name")
    
    store.setDefault(SAVE_BEFORE_COMPILE, true)
    store.setDefault(P_BOOLEAN, true)
    store.setDefault(P_CHOICE, "choice2")
    
    if(OS.contains("win")) {
      store.setDefault(RUST_C, RustOSConstants.windowsRustPath)
    } else if(OS.contains("mac") || OS.contains("unix")) {
      store.setDefault(RUST_C, RustOSConstants.macRustPath)
    }
    if(OS.contains("win")) {
      store.setDefault(P_PATH, RustOSConstants.windowsRustHome)
    } else {
      store.setDefault(P_PATH, "")
    }
    
    store.setDefault(RUNTIME_ARGS, "--out-dir")
    
  }
}

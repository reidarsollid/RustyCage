package org.rustycage.preferences

import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.rustycage.RustPlugin

class PreferenceInitializer extends AbstractPreferenceInitializer {
  protected def initializeDefaultPreferences() {
    import org.rustycage.preferences.PreferenceConstants._
    val store = RustPlugin.prefStore
    store.setDefault(SAVE_BEFORE_COMPILE, true)
    if(Platform.getOS.equalsIgnoreCase(Platform.OS_WIN32)){
      store.setDefault(RUST_C, "C:\\Rust\\bin\\")
    }else {
      store.setDefault(RUST_C, "/usr/local/bin/")
    }
    store.setDefault(RUST_HOME, "")
  }
}

package org.baksia.rustycage

import org.eclipse.core.runtime.Status
import org.eclipse.jface.preference.IPreferenceStore
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

object RustPlugin {
  @volatile var plugin: RustPlugin = _

  final val PluginId = "org.baksia.rustycage"

  def prefStore: IPreferenceStore = plugin.getPreferenceStore

  def getImageDescriptor(path: String): ImageDescriptor = {
    AbstractUIPlugin.imageDescriptorFromPlugin(PluginId, path)
  }

  def log(status: Int, msg: String, ex: Throwable = null): Unit = {
    plugin.getLog.log(new Status(status, plugin.getBundle.getSymbolicName, msg, ex))
  }
}

class RustPlugin extends AbstractUIPlugin {
  override def start(context: BundleContext) {
    super.start(context)
    RustPlugin.plugin = this
  }

  override def stop(context: BundleContext) {
    RustPlugin.plugin = null
    super.stop(context)
  }

}
package org.baksia.rustycage


import org.osgi.framework.BundleContext
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.plugin.AbstractUIPlugin


class RustPlugin extends AbstractUIPlugin {
	override def start(context: BundleContext) {
	  super.start(context)
	  RustPlugin.plugin = RustPlugin.this
	}
	
	override def stop(context: BundleContext) {
	  super.stop(context)
	  RustPlugin.plugin = null
	}
}

object RustPlugin {
  val PLUGIN_ID = "RustyCage"
  private var plugin : RustPlugin = null
  def getDefault() : RustPlugin = plugin
  
  def getImageDescriptor(path : String) : ImageDescriptor = {
    AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path)
  }
}
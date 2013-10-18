package org.baksia.rustycage.builder

import org.eclipse.core.resources.{IResource, IContainer, IncrementalProjectBuilder, IProject}
import java.util
import org.eclipse.core.runtime.IProgressMonitor
import org.baksia.rustycage.compile.RustyCageCompile

class RustBuilder extends IncrementalProjectBuilder {

  import IncrementalProjectBuilder._

  def build(kind: Int, args: util.Map[String, String], monitor: IProgressMonitor): Array[IProject] = {
    kind match {
      case INCREMENTAL_BUILD | AUTO_BUILD =>
        //We do nothing
        null
      case CLEAN_BUILD | FULL_BUILD =>
        val crate = findCrate(getProject.getFolder("src"))
       // RustyCageCompile.compile(crate, "", monitor, getProject)
        null
    }
  }

  //Move this function out a trait CrateFinder or in RustPlugin ??
  def findCrate(dir: IContainer): IResource = {
    val crates = dir.members().filter(r => r.getFileExtension == "rc")
    if (crates.length > 1)
      throw new RuntimeException
    else
      crates(0)
  }
}

object RustBuilder extends RustBuilder {
  val BUILDER_ID = "org.baksia.rustycage.rustBuilder"
  val MARKER_TYPE = "org.baksia.rustycage.xmlProblem"
}

package org.baksia.rustycage.builder

import org.eclipse.core.resources.{IProject, IProjectNature}

class RustNature extends IProjectNature {

  def configure() {
    val description = project.getDescription
    val commands = description.getBuildSpec
    if (!commands.contains(RustBuilder.BUILDER_ID)) {
      val newCommand = description.newCommand
      newCommand.setBuilderName(RustBuilder.BUILDER_ID)
      description.setBuildSpec(commands :+ newCommand)
    }
  }

  def deconfigure() {
    val description = project.getDescription
    val commands = description.getBuildSpec
    description.setBuildSpec(commands.filter(_.equals(RustBuilder.BUILDER_ID)))
    project.setDescription(description, null)
  }

  def getProject = project

  def setProject(project: IProject) {
    this.project = project
  }

  private var project: IProject = _
}

object RustNature extends RustNature {
  val NATURE_ID = "org.baksia.rustycage.rustNature"
}

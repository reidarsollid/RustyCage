package org.rustycage.builder

import org.eclipse.ui.{IWorkbenchPart, IObjectActionDelegate}
import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.{IStructuredSelection, ISelection}
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IAdaptable

class ToggleRustNature extends IObjectActionDelegate {
  def run(action: IAction) {
    selection match {
      case selections: IStructuredSelection =>
        selections.toArray.foreach {
          case project: IProject =>
            toggleNature(project)
          case adaptable: IAdaptable =>
            toggleNature(adaptable.getAdapter(classOf[IProject]).asInstanceOf[IProject])
        }
    }
  }

  def selectionChanged(action: IAction, selection: ISelection) {
    this.selection = selection
  }

  def setActivePart(action: IAction, targetPart: IWorkbenchPart) {

  }

  private def toggleNature(project: IProject) {
    val description = project.getDescription
    val natures = description.getNatureIds
    if (natures.contains(RustNature.NATURE_ID)) {
      description.setNatureIds(natures.filter(_.equals(RustNature.NATURE_ID)))
    } else {
      description.setNatureIds(natures :+ RustNature.NATURE_ID)
      project.setDescription(description, null)
    }
  }

  private var selection: ISelection = _
}

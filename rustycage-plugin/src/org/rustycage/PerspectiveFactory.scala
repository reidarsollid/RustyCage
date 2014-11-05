package org.rustycage

import org.eclipse.ui.{IPageLayout, IPerspectiveFactory}
import org.eclipse.ui.console.IConsoleConstants

//import org.eclipse.search.ui.NewSearchUI

import org.eclipse.ui.progress.IProgressConstants

class PerspectiveFactory extends IPerspectiveFactory {

  import org.rustycage.PerspectiveConstants._

  def createInitialLayout(pageLayout: IPageLayout) {
    val editorArea = pageLayout.getEditorArea
    //TODO: Rust buttons only to be visible for this perspective
    //TODO: Figure out what I am doing here and refactor to functions with good function names
    val folder = pageLayout.createFolder("left", IPageLayout.LEFT, 0.25F, editorArea)
    folder.addView(IPageLayout.ID_PROJECT_EXPLORER)
    folder.addView(IPageLayout.ID_RES_NAV)
    folder.addPlaceholder(IPageLayout.ID_RES_NAV)
    folder.addPlaceholder(ID_PROJECT_EXPLORER)

    val outputFolder = pageLayout.createFolder("bottom", IPageLayout.BOTTOM, 0.75F, editorArea)
    outputFolder.addView(IPageLayout.ID_PROBLEM_VIEW)
    outputFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW)
    //      outputfolder.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID)
    outputFolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW)
    outputFolder.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW)
    outputFolder.addPlaceholder(IPageLayout.ID_BOOKMARKS)
    outputFolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID)

    val outlineFolder = pageLayout.createFolder("right", IPageLayout.RIGHT, 0.75F, editorArea)
    outlineFolder.addView(IPageLayout.ID_OUTLINE)


    pageLayout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET)


    // views - search
    //     pageLayout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID)


    pageLayout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW)
    pageLayout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW)

    createStandardWorkbench(pageLayout)


    createRustProjectCreationWizard(pageLayout)
  }

  def createRustProjectCreationWizard(pageLayout: IPageLayout) {
    pageLayout.addNewWizardShortcut(RUST_PROJECT_WIZARD)
    pageLayout.addNewWizardShortcut(RUST_NEW_FILE_WIZARD)
    pageLayout.addNewWizardShortcut(NEW_FOLDER)
    pageLayout.addNewWizardShortcut(NEW_FILE)
    pageLayout.addNewWizardShortcut(UNTITLED_TEXT_FILE_WIZARD)
  }

  def createStandardWorkbench(pageLayout: IPageLayout) {
    pageLayout.addShowViewShortcut(IPageLayout.ID_OUTLINE)
    pageLayout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW)
    pageLayout.addShowViewShortcut(IPageLayout.ID_RES_NAV)
    pageLayout.addShowViewShortcut(IPageLayout.ID_TASK_LIST)
    pageLayout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID)
    pageLayout.addShowViewShortcut(ID_PROJECT_EXPLORER)
  }
}

object PerspectiveConstants {
  //val MENUE_ITEMS = "rustycage.actions.compileActions"
  val ID_PROJECT_EXPLORER = "org.eclipse.ui.navigator.ProjectExplorer"
  val RUST_PROJECT_WIZARD = "org.rustycage.wizards.RustProjectWizard"
  val RUST_NEW_FILE_WIZARD = "org.rustycage.wizards.RustNewFileWizard"
  val NEW_FOLDER = "org.eclipse.ui.wizards.new.folder"
  val NEW_FILE = "org.eclipse.ui.wizards.new.file"
  val UNTITLED_TEXT_FILE_WIZARD = "org.eclipse.ui.editors.wizards.UntitledTextFileWizard"
}
package org.baksia.rustycage;

import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class PerspectiveFactory implements IPerspectiveFactory {
    public static final String ID_PROJECT_EXPLORER = "org.eclipse.ui.navigator.ProjectExplorer";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea); //$NON-NLS-1$
        folder.addView(IPageLayout.ID_PROJECT_EXPLORER);
        folder.addView(IPageLayout.ID_RES_NAV);
        folder.addPlaceholder(IPageLayout.ID_RES_NAV);
        folder.addPlaceholder(ID_PROJECT_EXPLORER);

        IFolderLayout outputfolder = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea); //$NON-NLS-1$
//        outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
        outputfolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        outputfolder.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID);
        outputfolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
        outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
        outputfolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

        IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.RIGHT, (float) 0.75, editorArea); //$NON-NLS-1$
        outlineFolder.addView(IPageLayout.ID_OUTLINE);


        layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);


        // views - search
        layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);

        // views - debugging
        layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);

        // views - standard workbench
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
        layout.addShowViewShortcut(ID_PROJECT_EXPLORER);


        // new actions - Rust project creation wizard
        layout.addNewWizardShortcut("org.baksia.rustycage.wizards.RustProjectWizard"); //$NON-NLS-1$
        layout.addNewWizardShortcut("org.baksia.rustycage.wizards.RustNewFileWizard"); //$NON-NLS-1$
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
        layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
    }
}



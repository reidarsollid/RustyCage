package org.baksia.rustycage;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
        defineLayout(layout);
	}

    private void defineLayout(IPageLayout layout) {
        // Editors are placed for free.
        String editorArea = layout.getEditorArea();

        // Place navigator and outline to left of
        // editor area.
        IFolderLayout left =
                layout.createFolder("left", IPageLayout.LEFT, (float) 0.26, editorArea);
        left.addView(IPageLayout.ID_OUTLINE);
    }

    public void defineActions(IPageLayout layout) {
        layout.addNewWizardShortcut("org.baksia.rustycage.wizards.RustNewWizardPage");
        layout.addNewWizardShortcut("org.baksia.rustycage.wizards.RustNewWizard");

        layout.addShowViewShortcut(IPageLayout.ID_EDITOR_AREA);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        layout.addShowViewShortcut(IPageLayout.ID_NAVIGATE_ACTION_SET);
    }
    
}

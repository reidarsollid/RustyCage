package org.baksia.rustycage.editors;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;

public class RustEditor extends TextEditor {

    public RustEditor() {
        super();
        PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("org.baksia.rustycage.ui.theme");
        setSourceViewerConfiguration(new RustConfiguration());
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    protected void createActions() {
        super.createActions();
    }
}

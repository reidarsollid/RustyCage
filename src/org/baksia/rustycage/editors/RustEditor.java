package org.baksia.rustycage.editors;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;

public class RustEditor extends TextEditor {

    public RustEditor() {
        super();
//        addListenerObject();
        //TODO : chenge on focus gained and focus lost
        PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("org.baksia.rustycage.ui.theme");
        setSourceViewerConfiguration(new RustConfiguration());
    }

    @Override
    public void dispose() {
        //TODO : Figure out defauøt theme and chenge on focus gained and focus lost
        PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("Default");
        super.dispose();
    }

    @Override
    protected void createActions() {
        super.createActions();
    }

    @Override
    public void setFocus() {
        super.setFocus();
    }


}

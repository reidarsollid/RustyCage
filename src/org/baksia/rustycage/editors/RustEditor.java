package org.baksia.rustycage.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class RustEditor extends TextEditor {

    public RustEditor() {
        super();
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

package org.baksia.rustycage.run;

import org.baksia.rustycage.editors.RustEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

public class RustRun implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        RustEditor rustEditor = (RustEditor) window.getActivePage().getActiveEditor().getAdapter(ITextEditor.class);
        if (rustEditor != null) {
            rustEditor.getTitle();
            HackedRustRunner.run((IFile) rustEditor.getEditorInput().getAdapter(IFile.class));

        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

}

package org.baksia.rustycage.run;

import org.baksia.rustycage.editors.RustEditor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public class RustRun extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        RustEditor rustEditor = (RustEditor) window.getActivePage().getActiveEditor().getAdapter(ITextEditor.class);

        if (rustEditor != null) {
            HackedRustRunner.run((IFile) rustEditor.getEditorInput().getAdapter(IFile.class), window.getWorkbench().getProgressService());
        }
        return null;
    }

}

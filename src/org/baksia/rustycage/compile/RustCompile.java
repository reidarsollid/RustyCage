package org.baksia.rustycage.compile;

import org.baksia.rustycage.Activator;
import org.baksia.rustycage.editors.RustEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

public class RustCompile implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    @Override
    public void run(IAction action) {
        RustEditor rustEditor = (RustEditor) window.getActivePage().getActiveEditor().getAdapter(ITextEditor.class);
        IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
        boolean isLib = preferenceStore.getBoolean("IsLib");
        if (rustEditor != null) {
            IResource iResource = (IResource) rustEditor.getEditorInput().getAdapter(IResource.class);
            IPath fullPath = iResource.getFullPath();
            String argument = "";
            if (fullPath.toString().contains("test") && !fullPath.toString().contains("src")) {
                argument = "--test ";
            } else if (isLib) {
                argument = "--lib ";
            }
            HackedRustCompiler.compile((IFile) rustEditor.getEditorInput().getAdapter(IFile.class), argument);
        }

    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {

        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {

    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}

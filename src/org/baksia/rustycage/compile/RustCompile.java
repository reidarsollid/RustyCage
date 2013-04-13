package org.baksia.rustycage.compile;


import org.baksia.rustycage.RustPlugin;
import org.baksia.rustycage.editors.RustEditor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public class RustCompile extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        RustEditor rustEditor = (RustEditor) window.getActivePage().getActiveEditor().getAdapter(ITextEditor.class);

        IPreferenceStore preferenceStore = RustPlugin.getDefault().getPreferenceStore();
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
            HackedRustCompiler.compile((IFile) rustEditor.getEditorInput().getAdapter(IFile.class), argument, null);
        }
        return null;
    }

}

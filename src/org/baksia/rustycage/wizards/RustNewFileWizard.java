package org.baksia.rustycage.wizards;

import org.baksia.rustycage.RustPlugin;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class RustNewFileWizard extends Wizard implements INewWizard {
    private RustNewFileWizardPage pageFile;
    private ISelection selection;

    public RustNewFileWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        pageFile = new RustNewFileWizardPage(selection);
        addPage(pageFile);
    }

    @Override
    public boolean performFinish() {
        final String containerName = pageFile.getContainerName();
        final String fileName = pageFile.getFileName();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerName, fileName, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
        // create a sample file
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + containerName + "\" does not exist.");
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        IPreferenceStore preferenceStore = RustPlugin.getDefault().getPreferenceStore();
        String projectName = preferenceStore.getString("ProjectName");
        IFile crateFile = container.getFile(new Path(projectName + ".rc"));

        try {
            InputStream stream = openContentStream();
            InputStream crateStream = openCrateContentStream(fileName);
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            if (crateFile.exists()) {
                crateFile.appendContents(crateStream, true, true, monitor);
            }
            crateStream.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        monitor.worked(1);
        monitor.setTaskName("Opening file for editing...");
        getShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
                IWorkbenchPage page =
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(page, file, true);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
        monitor.worked(1);
    }

    private InputStream openCrateContentStream(String fileName) {
        String modName = fileName.substring(0, fileName.length() - 3);
        String contents = "\nmod " + modName + ";";
        return new ByteArrayInputStream(contents.getBytes());
    }

    private InputStream openContentStream() {
        String contents =
                "/*This file is generated with RustyCage*/";
        return new ByteArrayInputStream(contents.getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status =
                new Status(IStatus.ERROR, "RustyCage", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
package org.baksia.rustycage.compile;

import org.baksia.rustycage.Activator;
import org.baksia.rustycage.preferences.PreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.IOException;
import java.util.Scanner;


public final class HackedRustCompiler {
    private static final String MARKER_TYPE = "org.eclipse.core.resources.problemmarker";

    private HackedRustCompiler() {
    }

    public static boolean compile(IFile file, String argument) {

        try {
            //String time_passes = " --time-passes";
            clearMarkers(file);
            IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
            String rustPath = preferenceStore.getString(PreferenceConstants.RUST_C);

            String rustProject = preferenceStore.getString(PreferenceConstants.P_PATH);

            String projectName = preferenceStore.getString("ProjectName");
            String rawPath = file.getRawLocationURI().getRawPath();
            int endIndex = rawPath.indexOf(projectName);
            String src = "";
            if(endIndex != -1 ) {
            	String home = rawPath.substring(0, endIndex);
                src = " -L " + home +projectName +"/src";
            }
             

            Process exec = Runtime.getRuntime().exec(rustPath + "rustc " + argument + rawPath +  src);
            //+ " --out-dir bin");
            Scanner scanner = new Scanner(exec.getInputStream());
            Scanner errorScanner = new Scanner(exec.getErrorStream());
            MessageConsole messageConsole = new MessageConsole("Rustc", null);

            ConsolePlugin.getDefault().getConsoleManager().addConsoles(
                    new IConsole[]{messageConsole});

            MessageConsoleStream messageConsoleStream = messageConsole.newMessageStream();
            messageConsoleStream.println("Compiling " + file);
            messageConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            while (scanner.hasNextLine()) {
                messageConsoleStream.println(scanner.nextLine());
            }
            messageConsoleStream.close();
            messageConsoleStream = messageConsole.newMessageStream();

            messageConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

            while (errorScanner.hasNextLine()) {
                String firstLine = errorScanner.nextLine();
                IFile theFile = findCorrectFile(firstLine, file);
                clearMarkers(theFile);
                parseProblemFirstLine(firstLine, theFile);
                messageConsoleStream.println(firstLine);
                if (!errorScanner.hasNextLine()) {
                    break;
                }
                String secondLine = errorScanner.nextLine();
                parseProblemSecondLine(secondLine, theFile);
                messageConsoleStream.println(secondLine);
                if (!errorScanner.hasNextLine()) {
                    break;
                }
                messageConsoleStream.println(errorScanner.nextLine());

            }

            messageConsoleStream.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (CoreException e) {
            return false;
        }

    }

    private static IFile findCorrectFile(String firstLine, IFile file) throws CoreException {
        if (file.getFileExtension().equals("rc")) {
            IResource[] resources = file.getParent().members();
            for (IResource resource : resources) {
                if (firstLine.contains(resource.getName())) {
                    return (IFile) resource;
                }
            }
            //this is not the file we want to mark IResource members[] = aFolder.members();
        }
        return file;
    }

    private static void parseProblemSecondLine(String errorString, IFile file) {
        //Ignore for now
    }

    private static void clearMarkers(IFile file) {
        try {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
        } catch (CoreException e) {
            Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
        }
    }

    private static void parseProblemFirstLine(String errorString, IFile file) {
        if (errorString.contains(".rs")) {
            ///home/reidar/runtime-EclipseApplication/RustProject/src/new_file.rs:4:1: 4:8 error: unresolved name: println
            String tokens[] = errorString.split(":");
            String markerString = tokens[tokens.length - 1] + ": " + tokens[tokens.length - 2];
            int lineNumber = 0;
            try {
                lineNumber = Integer.parseInt(tokens[1]);
            } catch (NumberFormatException exception) {
                lineNumber = 0;
            }
            addMarker(file, markerString, lineNumber, IMarker.SEVERITY_ERROR);
        }
    }

    private static void addMarker(IFile file, String message, int lineNumber, int severity) {
        try {
            IMarker marker = file.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY, severity);

            if (lineNumber == -1) {
                lineNumber = 1;
            }
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);

        } catch (CoreException e) {
            Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
        }
    }

}

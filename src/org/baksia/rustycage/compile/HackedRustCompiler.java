package org.baksia.rustycage.compile;

import org.baksia.rustycage.RustPlugin;
import org.baksia.rustycage.preferences.PreferenceConstants$;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public final class HackedRustCompiler {
	private static ProblemMarker problemMarker = new ProblemMarker();

	private HackedRustCompiler() {

	}

	public static boolean compile(IFile file, String argument,
			IProgressMonitor monitor) {

		try {
			// String time_passes = " --time-passes";
			problemMarker.clearMarkers(file);
			IPreferenceStore preferenceStore = RustPlugin.getDefault()
					.getPreferenceStore();
			String rustPath = preferenceStore
					.getString(PreferenceConstants$.MODULE$.RUST_C());

			// String rustProject =
			// preferenceStore.getString(PreferenceConstants.P_PATH);

			String projectName = preferenceStore.getString("ProjectName");
			String rawPath = file.getRawLocationURI().getRawPath();
			int endIndex = rawPath.indexOf(projectName);
			String src = "";
			String bin = "";
			if (endIndex != -1) {
				String home = rawPath.substring(0, endIndex);
				bin = home + projectName + "/.bin";
				src = " -L " + home + projectName + "/src";
			}

			// RuntimeProcess runtimeProcess = new RuntimeProcess();
			// DebugPlugin.exec(new String[]{rustPath + "rustc " , argument},new
			// File(rustProject));            
            new File(bin).mkdir();
			Process exec = Runtime.getRuntime().exec(
					rustPath + "rustc " + argument + rawPath + src+" --out-dir " + bin);
			// ProcessConsole processConsole = new ProcessConsole(exec,null);

			// + " --out-dir bin");
			Scanner scanner = new Scanner(exec.getInputStream());
			Scanner errorScanner = new Scanner(exec.getErrorStream());
			MessageConsole messageConsole = new MessageConsole("Rustc", null);
			messageConsole.activate();
			ConsolePlugin.getDefault().getConsoleManager()
					.addConsoles(new IConsole[] { messageConsole });

			writeToConsole(file, scanner, errorScanner, messageConsole);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

	}

	private static void writeToConsole(IFile file, Scanner scanner,
			Scanner errorScanner, MessageConsole messageConsole)
			throws IOException, CoreException {
		MessageConsoleStream messageConsoleStream = messageConsole
				.newMessageStream();
		messageConsoleStream.println("Compiling " + file);
		messageConsoleStream.setColor(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK));
		while (scanner.hasNextLine()) {
			messageConsoleStream.println(scanner.nextLine());
		}
		messageConsoleStream.close();
		messageConsoleStream = messageConsole.newMessageStream();

		messageConsoleStream.setColor(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		while (errorScanner.hasNextLine()) {
			String firstLine = errorScanner.nextLine();
			IFile theFile = findCorrectFile(firstLine, file);
			
			problemMarker.parseProblemFirstLine(firstLine, theFile);
			messageConsoleStream.println(firstLine);
			if (!errorScanner.hasNextLine()) {
				break;
			}
			String secondLine = errorScanner.nextLine();
			problemMarker.parseProblemSecondLine(secondLine, theFile);
			messageConsoleStream.println(secondLine);
			if (!errorScanner.hasNextLine()) {
				break;
			}
			messageConsoleStream.println(errorScanner.nextLine());

		}

		messageConsoleStream.close();
	}

	private static IFile findCorrectFile(String firstLine, IFile file)
			throws CoreException {
		if (file.getFileExtension().equals("rc")) {
			IResource[] resources = file.getParent().members();
			for (IResource resource : resources) {
				if (firstLine.contains(resource.getName())) {
					return (IFile) resource;
				}
			}
			// this is not the file we want to mark IResource members[] =
			// aFolder.members();
		}
		return file;
	}	
	
}

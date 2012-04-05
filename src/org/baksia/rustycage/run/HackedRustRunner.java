package org.baksia.rustycage.run;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.IOException;
import java.util.Scanner;

public class HackedRustRunner {
    public static void run(IFile iFile) {
        Process exec = null;
        try {
            String rawPath = iFile.getRawLocationURI().getRawPath();
            String file = rawPath.substring(0, rawPath.lastIndexOf("."));
            //TODO : Need a stop button to kill process
            exec = Runtime.getRuntime().exec(file);
            Scanner scanner = new Scanner(exec.getInputStream());
            Scanner errorScanner = new Scanner(exec.getErrorStream());
            MessageConsole messageConsole = new MessageConsole("Rust run", null);

            ConsolePlugin.getDefault().getConsoleManager().addConsoles(
                    new IConsole[]{messageConsole});

            /*
                IPath path = Path.fromOSString(filePath);
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
             */
            MessageConsoleStream messageConsoleStream = messageConsole.newMessageStream();
            messageConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            messageConsoleStream.println("Running: " + file);
            while (scanner.hasNextLine()) {
                messageConsoleStream.println(scanner.nextLine());
            }
            messageConsoleStream.close();
            messageConsoleStream = messageConsole.newMessageStream();
            messageConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
            while (errorScanner.hasNextLine()) {
                messageConsoleStream.println(errorScanner.nextLine());
            }
            messageConsoleStream.close();

        } catch (IOException e) {
            if (exec != null) {
                exec.destroy();
            }
        } finally {
            if (exec != null) {
                exec.destroy();
            }
        }
    }
}

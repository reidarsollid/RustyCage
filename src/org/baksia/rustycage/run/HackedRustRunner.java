package org.baksia.rustycage.run;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.IOException;
import java.util.Scanner;

public class HackedRustRunner {
    public static void run(IFile iFile) {
        try {
            String rawPath = iFile.getRawLocationURI().getRawPath();
            String file = rawPath.substring(0, rawPath.lastIndexOf("."));
            Process exec = Runtime.getRuntime().exec(file);
            Scanner scanner = new Scanner(exec.getInputStream());
            Scanner errorScanner = new Scanner(exec.getErrorStream());
            MessageConsole messageConsole = new MessageConsole("Rust run", null);
            ConsolePlugin.getDefault().getConsoleManager().addConsoles(
                    new IConsole[]{messageConsole});

            MessageConsoleStream messageConsoleStream = messageConsole.newMessageStream();
            messageConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

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
            System.out.println(e.getMessage());
        }
    }
}

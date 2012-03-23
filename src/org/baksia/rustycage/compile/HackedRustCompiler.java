package org.baksia.rustycage.compile;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.IOException;
import java.util.Scanner;


public final class HackedRustCompiler {
    private HackedRustCompiler() {
    }

    public static boolean compile(IFile file) {

        try {
            Process exec = Runtime.getRuntime().exec("/usr/local/bin/rustc " + file.getRawLocationURI().getRawPath());
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
                messageConsoleStream.println(errorScanner.nextLine());
            }

            messageConsoleStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

}

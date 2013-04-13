package org.baksia.rustycage.editors;

import org.baksia.rustycage.RustPlugin;
import org.baksia.rustycage.preferences.PreferenceConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RustContentAssistProcessor implements IContentAssistProcessor {

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        IDocument document = viewer.getDocument();
        List<ICompletionProposal> result = new ArrayList<>(Parser.KEYWORDS.length);
        String typedString = getTypedString(document, offset);
        if (typedString.contains("::")) {
            fetchLibProposals(typedString, result, offset);
        } else {
            for (String keyword : Parser.KEYWORDS) {
                if (keyword.startsWith(typedString)) {
                    //   IContextInformation info = new ContextInformation(keyword, "Rust keyword");
                    result.add(new CompletionProposal(keyword.trim(), offset - typedString.length(), typedString.length(), keyword.length()));
                }
            }
            //TODO: Try sort string array before creating CompletionProposal
            IPreferenceStore preferenceStore = RustPlugin.getDefault().getPreferenceStore();
            String rustPathCrate = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libcore/core.rc";
            String rustPathStdCrate = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd/std.rc";
            createCrateProposals(result, offset, typedString, rustPathCrate, rustPathStdCrate);


        }
        ICompletionProposal[] iCompletionProposals = new ICompletionProposal[result.size()];
        return result.toArray(iCompletionProposals);
    }

    private void createCrateProposals(List<ICompletionProposal> result, int offset, String typedString, String... files) {
        for (String file : files) {
            File aFile = new File(file);
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(aFile))) {
                String readLine;
                while ((readLine = bufferedReader.readLine()) != null) {
                    if (readLine.startsWith("export")) {
                        String export = readLine.replace("export", "").replace(";", "");
                        for (String token : export.split(",")) {
                            String module = token.trim();
                            result.add(new CompletionProposal(module, offset - typedString.length(), typedString.length(), module.length()));
                        }

                    }
                }
            } catch (IOException e) {
                RustPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, RustPlugin.PLUGIN_ID(), e.getMessage()));
            }
        }
    }

    private void fetchLibProposals(String docString, List<ICompletionProposal> result, int offset) {
        String[] tokens = docString.split("::");
        String word = "";
        if (!(tokens.length < 2)) {
            word = tokens[1];
        }
        String lib = tokens[0];


        IPreferenceStore preferenceStore = RustPlugin.getDefault().getPreferenceStore();
        String rustPath = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libcore";
        String rustPathStd = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd";

        createProposals(result, offset, word, lib, rustPath);
        createProposals(result, offset, word, lib, rustPathStd);


    }

    private void createProposals(List<ICompletionProposal> result, int offset, String word, String lib, String rustPath) {
        if (rustPath == null) {
            return;
        }
        File libDir = new File(rustPath);
        File[] files = libDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".rs") && file.getName().startsWith(lib)) {

                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                        String readLine;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            if (readLine.startsWith("fn") && readLine.contains(word)) {
                                String token = readLine.split("fn")[1];
                                if (token.contains("(")) {
                                    IContextInformation info = new ContextInformation(token, lib);
                                    String resultWord = token.substring(0, token.indexOf("(")).trim();
                                    String displayString = token;
                                    if (token.contains("{")) {
                                        displayString = token.substring(0, token.indexOf("{"));
                                    }
                                    //TODO : This is butt ugly, but shows me what I need from the lib
                                    result.add(new CompletionProposal(resultWord, offset - word.length(), word.length(), resultWord.length(), null, displayString, info, readLine));
                                }
                            }
                        }
                    } catch (IOException handled) {
                        RustPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, RustPlugin.PLUGIN_ID(), handled.getMessage()));
                    }
                }
            }
        }
    }

    private String getTypedString(IDocument document, int offset) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while (true) {
                char c = document.getChar(--offset);
                if (Character.isWhitespace(c)) {
                    return stringBuilder.reverse().toString();
                }
                stringBuilder.append(c);
            }

        } catch (BadLocationException e) {
            return stringBuilder.toString();
        }

    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        //TODO : Need to activate from ::, look at CDT ?
        return new char[]{':', ':'};
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return new char[]{':', ':'};
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        IContextInformationValidator iContextInformationValidator = new RustParameterListParameter();

        return iContextInformationValidator;
    }
}

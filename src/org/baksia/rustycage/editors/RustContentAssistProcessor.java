package org.baksia.rustycage.editors;

import org.baksia.rustycage.Activator;
import org.baksia.rustycage.preferences.PreferenceConstants;
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

/**
 * User: Reidar Sollid
 * Date: 08.03.12
 * Time: 14:20
 */
public class RustContentAssistProcessor implements IContentAssistProcessor {

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        IDocument document = viewer.getDocument();
        List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(Parser.KEYWORDS.length);
        String docString = getTypedString(document, offset);
        if (docString.contains("::")) {
            fetchLibProposals(docString, result, offset);
        } else {
            for (String keyword : Parser.KEYWORDS) {
                if (keyword.startsWith(docString)) {
                    //   IContextInformation info = new ContextInformation(keyword, "Rust keyword");
                    result.add(new CompletionProposal(keyword, offset - docString.length(), docString.length(), (keyword.length() - 1)));
                }
            }
        }
        ICompletionProposal[] iCompletionProposals = new ICompletionProposal[result.size()];
        return result.toArray(iCompletionProposals);
    }

    private void fetchLibProposals(String docString, List<ICompletionProposal> result, int offset) {
        String[] tokens = docString.split("::");
        String word = "";
        if (!(tokens.length < 2)) {
            word = tokens[1];
        }
        String lib = tokens[0];


        IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
        String rustPath = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libcore";
        String rustPathStd = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd";
        String rustPathUv = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libuv";
        createProposals(result, offset, word, lib, rustPath);
        createProposals(result, offset, word, lib, rustPathStd);
        createProposals(result, offset, word, lib, rustPathUv);

    }

    private void createProposals(List<ICompletionProposal> result, int offset, String word, String lib, String rustPath) {
        if (rustPath == null) {
            return;
        }
        File libDir = new File(rustPath);
        for (File file : libDir.listFiles()) {
            if (file.getName().endsWith(".rs") && file.getName().startsWith(lib)) {
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    String readLine = null;
                    while ((readLine = bufferedReader.readLine()) != null) {
                        if (readLine.startsWith("fn") && readLine.contains(word)) {
                            String token = readLine.split("fn")[1];
                            if (token.contains("(")) {
                                IContextInformation info = new ContextInformation(token, lib);
                                String resultWord = token.substring(0, token.indexOf("("));
                                String displayString = token;
                                if (token.contains("{")) {
                                    displayString = token.substring(0, token.indexOf("{"));
                                }
                                //TODO : This is butt ugly, but shows me what I need from the lib
                                result.add(new CompletionProposal(resultWord, offset - (word.length() + 1), word.length(), resultWord.length(), null, displayString, info, readLine));
                            }
                        }
                    }
                } catch (IOException handled) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ignored) {

                    }
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException ignored) {

                        }
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

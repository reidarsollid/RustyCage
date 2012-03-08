package org.baksia.rustycage.editors;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * User: Reidar Sollid
 * Date: 08.03.12
 * Time: 15:55
 */
public class RustParameterListParameter implements IContextInformationValidator {
    private int offset;
    private ITextViewer viewer;
    private IContextInformation info;

    @Override
    public void install(IContextInformation info, ITextViewer viewer, int offset) {
        this.info = info;
        this.viewer = viewer;
        this.offset = offset;
    }

    @Override
    public boolean isContextInformationValid(int offset) {
        return true;
    }
}

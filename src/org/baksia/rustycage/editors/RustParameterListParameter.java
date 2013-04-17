package org.baksia.rustycage.editors;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class RustParameterListParameter implements IContextInformationValidator,IContextInformationPresenter {
    private int offset;
    private ITextViewer viewer;
    private IContextInformation info;
    protected int fInstallOffset;


    @Override
    public void install(IContextInformation info, ITextViewer viewer, int offset) {
        this.info = info;
        this.viewer = viewer;
        this.fInstallOffset = offset;
    }

    @Override
    public boolean updatePresentation(int offset, TextPresentation presentation) {
        return false;
    }

    @Override
    public boolean isContextInformationValid(int offset) {
        return Math.abs(fInstallOffset - offset) < 5;
    }

}

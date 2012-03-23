package org.baksia.rustycage;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;

import java.io.IOException;
import java.io.InputStream;

public class RustConentDescriber implements IContentDescriber {

    public RustConentDescriber() {
    }


    @Override
    public int describe(InputStream contents, IContentDescription description) throws IOException {
        return 0;
    }

    @Override
    public QualifiedName[] getSupportedOptions() {
        return new QualifiedName[0];
    }
}

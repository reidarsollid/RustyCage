package org.baksia.rustycage.editors;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class RustColorConstants {
    public static final Color KEYWORD = new Color(Display.getCurrent(), new RGB(160, 82, 45));
    public static final Color NEW_FILE = new Color(Display.getCurrent(), new RGB(210, 105, 30));
    public static final Color COMMENT = new Color(Display.getCurrent(), new RGB(188, 143, 143));
    public static final Color STRING = new Color(Display.getCurrent(), new RGB(70, 130, 180)); //new RGB(119, 136, 153));
    public static final Color NUMBERS = new Color(Display.getCurrent(), new RGB(104, 131, 139));
    public static final Color CONTENT_ASSIST = new Color(Display.getCurrent(), new RGB(220, 220, 220));
}

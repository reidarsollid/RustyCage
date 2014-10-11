package org.rustycage.editors

import org.eclipse.swt.graphics.{Color, RGB}
import org.eclipse.swt.widgets.Display

object RustColorConstants {
  val KEYWORD = new Color(Display.getCurrent, new RGB(160, 82, 45))
  val NEW_FILE = new Color(Display.getCurrent, new RGB(210, 105, 30))
  val COMMENT = new Color(Display.getCurrent, new RGB(188, 143, 143))
  val STRING = new Color(Display.getCurrent, new RGB(70, 130, 180)) //new RGB(119, 136, 153)
  val NUMBERS = new Color(Display.getCurrent, new RGB(104, 131, 139))
  val CONTENT_ASSIST = new Color(Display.getCurrent, new RGB(220, 220, 220))
  val POINTER = new Color(Display.getCurrent, new RGB(205, 92, 92))
  val CHAR = new Color(Display.getCurrent, new RGB(61, 89, 171))
}
package org.baksia.rustycage

import org.eclipse.core.runtime.content.{IContentDescription, IContentDescriber}
import java.io.InputStream
import org.eclipse.core.runtime.QualifiedName

class RustContentDescriber extends IContentDescriber {
  def describe(contents: InputStream, description: IContentDescription) : Int = 0

  def getSupportedOptions = Array[QualifiedName]()

}

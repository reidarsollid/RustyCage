package org.rustycage.editors

import org.eclipse.jface.text.rules.IRule
import org.eclipse.jface.text.rules.ICharacterScanner
import org.eclipse.jface.text.rules.IToken
import org.eclipse.jface.text.rules.Token

class RustPointerRule(token: IToken) extends IRule {
  val POINTERS = Set('@', '~', '&')

  def isPointer(character: Char): Boolean = POINTERS.contains(character)

  override def evaluate(charScanner: ICharacterScanner): IToken = {
    var character: Char = charScanner.read.toChar
    var isPointerChar: IToken = Token.UNDEFINED
    if (!Character.isDigit(character)) {
      if (isPointer(character)) {
        isPointerChar = token
        do {
          character = charScanner.read.toChar
        } while (isPointer(character))
      }
    }
    charScanner.unread()
    isPointerChar
  }
}
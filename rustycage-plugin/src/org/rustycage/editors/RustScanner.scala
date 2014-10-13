package org.rustycage.editors

import org.eclipse.jface.text.rules._
import org.eclipse.jface.text.TextAttribute
import org.eclipse.swt.SWT

import org.rustycage.RustTextConstants
import org.rustycage.RustConstants

class RustScanner extends RuleBasedScanner {
  val rule: WordRule = new WordRule(new IWordDetector() {

    def isWordStart(c: Char) = Character.isJavaIdentifierStart(c)

    def isWordPart(c: Char) = Character.isJavaIdentifierPart(c)

  }, Token.WHITESPACE)

  RustConstants.Keywords.foreach(keyword =>
    rule.addWord(keyword, RustTextConstants.keywordToken)
  )

  RustConstants.NewFile.foreach(newFile =>
    rule.addWord(newFile, RustTextConstants.file)
  )

  def whiteSpaceRule = new WhitespaceRule(new IWhitespaceDetector() {
    def isWhitespace(c: Char) = Character.isWhitespace(c)
  })

  val rules = Array[IRule](rule,
    new RustPointerRule(RustTextConstants.pointer),
    new NumberRule(RustTextConstants.numbers),
    new EndOfLineRule("//", RustTextConstants.comment, '\\'),
    new SingleLineRule("\"", "\"", RustTextConstants.string, '\\'),
    new SingleLineRule("\'", "\'", RustTextConstants.chars, '\\'),
    new MultiLineRule("/*", "*/", RustTextConstants.comment, '\\'),
    whiteSpaceRule)
    
    setRules(rules)
}

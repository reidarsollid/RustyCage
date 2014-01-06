package org.rustycage.editors

import org.eclipse.jface.text.rules._
import org.eclipse.jface.text.TextAttribute
import org.eclipse.swt.SWT

class RustScanner extends RuleBasedScanner {
  val rule: WordRule = new WordRule(new IWordDetector() {

    def isWordStart(c: Char) = Character.isJavaIdentifierStart(c)

    def isWordPart(c: Char) = Character.isJavaIdentifierPart(c)

  }, Token.WHITESPACE)

  val comment = new Token(new TextAttribute(RustColorConstants.COMMENT))
  val string = new Token(new TextAttribute(RustColorConstants.STRING))
  val chars = new Token(new TextAttribute(RustColorConstants.CHAR))
  val keywordToken = new Token(new TextAttribute(RustColorConstants.KEYWORD, null, SWT.BOLD))
  val file = new Token(new TextAttribute(RustColorConstants.NEW_FILE, null, SWT.ITALIC))
  val numbers = new Token(new TextAttribute(RustColorConstants.NUMBERS))
  val pointer = new Token(new TextAttribute(RustColorConstants.POINTER))

  RustParser.Keywords.foreach(keyword =>
    rule.addWord(keyword, keywordToken)
  )

  RustParser.NewFile.foreach(newFile =>
    rule.addWord(newFile, file)
  )

  def whiteSpaceRule = new WhitespaceRule(new IWhitespaceDetector() {
    def isWhitespace(c: Char) = Character.isWhitespace(c)
  })

  val rules = Array[IRule](rule,
    new RustPointerRule(pointer),
    new NumberRule(numbers),
    new EndOfLineRule("//", comment, '\\'),
    new SingleLineRule("\"", "\"", string, '\\'),
    new SingleLineRule("\'", "\'", chars, '\\'),
    new MultiLineRule("/*", "*/", comment, '\\'),
    whiteSpaceRule)
    
    setRules(rules)
}

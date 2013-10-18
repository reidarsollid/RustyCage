package org.baksia.rustycage.editors

import org.eclipse.jface.text.rules._

class RustPartitionScanner extends RuleBasedPartitionScanner {
  val comment = new Token("__rust_comment")
  val rules: Array[IPredicateRule] = Array(new MultiLineRule("/*", "*/", comment))
  setPredicateRules(rules)
}
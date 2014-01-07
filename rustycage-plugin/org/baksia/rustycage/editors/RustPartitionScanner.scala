package org.rustycage.editors


class RustPartitionScanner extends RuleBasedPartitionScanner {
  val comment = new Token("__rust_comment")
  val rules: Array[IPredicateRule] = Array(new MultiLineRule("/*", "*/", comment))
  setPredicateRules(rules)
}
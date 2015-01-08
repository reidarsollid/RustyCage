package org.rustycage.editors

class StringSplitToTuple(s: String) {
  def splitToTuple(regex: String): (String, String) = {
    s.split(regex) match {
      case Array(str1, str2) => (str1, str2)
      case Array(str1) => (str1, "")
      case _ => sys.error("too many colons in splitToTuple")
    }
  }

}

object SplitToTuple {
  implicit def splitToTuple(regex: String): StringSplitToTuple = new StringSplitToTuple(regex)
}

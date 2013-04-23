package org.baksia.rustycage.editors

class StringSplitToTuple(s: String) {
  def splitToTuple(regex: String): (String, String) = {
    s.split(regex) match {
      case Array(str1, str2) => (str1, str2)
      case Array(str1) => (str1, "")
      case _ => error("too many colons")
    }
  }

}

object SplitToTuple {
  implicit def splitToTuple(regex: String) = new StringSplitToTuple(regex)
}
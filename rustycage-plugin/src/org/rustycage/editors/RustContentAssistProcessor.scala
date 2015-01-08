package org.rustycage.editors

import java.io.File

import org.eclipse.jface.preference.IPreferenceStore
import org.eclipse.jface.text.contentassist.{CompletionProposal, ContextInformation, ICompletionProposal, IContentAssistProcessor, IContextInformation}
import org.eclipse.jface.text.{IDocument, ITextViewer}
import org.rustycage.RustPlugin
import org.rustycage.preferences.PreferenceConstants

import scala.annotation.tailrec
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.io.Source

//TODO: Replace this with racer ?
class RustContentAssistProcessor extends IContentAssistProcessor {

  def computeContextInformation(viewer: ITextViewer, offset: Int): Array[IContextInformation] = null

  import org.rustycage.editors.SplitToTuple._

  override def computeCompletionProposals(viewer: ITextViewer, offset: Int): Array[ICompletionProposal] = {
    val document = viewer.getDocument
    val typedString = getTypedString(document, offset)

    val proposalBuffer: ArrayBuffer[ICompletionProposal] = getKeywordCompletionProposals(offset, typedString)
    proposalBuffer.toArray
  }

  def getKeywordCompletionProposals(offset: Int, typedString: String): ArrayBuffer[ICompletionProposal] = {
    //KeyWord completions
    val proposalBuffer = new ArrayBuffer[ICompletionProposal]()
    if (typedString.contains("::")) {
      val libWord = typedString.splitToTuple("::")
      fetchLibProposals(typedString, proposalBuffer, offset, libWord)
    } else {
      //Add proposals from src/core/prelude
      val preferenceStore = RustPlugin.prefStore
      val preludePath = getRustSourceFullPath(preferenceStore, "/src/libstd/prelude.rs")

      /*   RustParser.Keywords.foreach(keyword =>
           if (keyword.startsWith(typedString))
             proposalBuffer += new CompletionProposal(keyword.trim(), offset - typedString.length(), typedString.length(), keyword.length()))
     */
      Source.fromFile(preludePath, "UTF-8").getLines().toList.foreach(newLine => {
        val line = newLine.trim()

        if (line.startsWith("pub use") && line.contains(typedString) && !line.contains("test")) {
          val token = line.replace("pub use", "")
          if (token.contains("::")) {
            val cleanToken = token.replace("{", "").replace("}", "")
            val props = cleanToken.split("::").last.split(",")

            props.foreach(wordDirty => {
              val word = wordDirty.trim
              if (word.startsWith(typedString)) {
                val proposal = word.replace(";", "")
                proposalBuffer += new CompletionProposal(proposal.trim(), offset - typedString.length(), typedString.length(), proposal.length())
              }
            })
          } else {
            val proposal = token.replace(";", "")
            proposalBuffer += new CompletionProposal(proposal.trim(), offset - typedString.length(), typedString.length(), proposal.length())
          }
        }
      })
    }
    proposalBuffer
  }

  private def fetchLibProposals(typedString: String, proposalsdBuffer: ArrayBuffer[ICompletionProposal], offset: Int, libWord: (String, String)) {
    val word = libWord._2
    val lib = libWord._1

    val list: List[File] = getRustSorcePath(lib)

    if (list.nonEmpty) {
      Source.fromFile(list(0), "UTF-8").getLines().toList.foreach(newLine => {

        val line = newLine.trim()

        if (line.startsWith("pub fn") && line.contains(word) && !line.contains("test")) {

          val token = line.replace("pub fn", "")

          if (token.contains("(")) {
            val info = new ContextInformation(token, lib)
            val resultWord = token.substring(0, token.indexOf("(")).trim()

            var displayString = token
            if (token.contains("{")) {
              displayString = token.substring(0, token.indexOf("{"))
            }

            //TODO : This is butt ugly, but shows me what I need from the lib
            proposalsdBuffer += new CompletionProposal(resultWord, offset - word.length(), word.length(), resultWord.length(), null, displayString, info, line)
          }
        }
      })
    }
  }

  private[RustContentAssistProcessor] def getRustSorcePath(lib: String): List[File] = {
    val preferenceStore = RustPlugin.prefStore
    val rustPathCollections = getRustSourceFullPath(preferenceStore, "/src/libcollections")
    val rustPathRand = getRustSourceFullPath(preferenceStore, "/src/librand")
    val rustPathLog = getRustSourceFullPath(preferenceStore, "/src/liblog")
    val rustPathArena = getRustSourceFullPath(preferenceStore, "/src/libarena")
    val rustPathFlate = getRustSourceFullPath(preferenceStore, "/src/libflate")
    val rustPathGetOpts = getRustSourceFullPath(preferenceStore, "/src/libgetopts")
    val rustPathSerializer = getRustSourceFullPath(preferenceStore, "/src/libserialize")
    val rustPathSync = getRustSourceFullPath(preferenceStore, "/src/libsync")
    val rustPathLibTerm = getRustSourceFullPath(preferenceStore, "/src/libterm")
    val rustPathLibTest = getRustSourceFullPath(preferenceStore, "/src/libtest")
    val rustPathLibTime = getRustSourceFullPath(preferenceStore, "/src/libtime")
    val rustPathLibWorkCache = getRustSourceFullPath(preferenceStore, "/src/libworkcache")
    val rustPathStd = getRustSourceFullPath(preferenceStore, "/src/libstd")

    val list = findFiles(lib, rustPathCollections, rustPathStd, rustPathRand, rustPathArena
      , rustPathFlate, rustPathGetOpts, rustPathSerializer, rustPathSync,
      rustPathLibTerm, rustPathLog, rustPathLibTest, rustPathLibTime, rustPathLibWorkCache)
    list
  }

  def getRustSourceFullPath(preferenceStore: IPreferenceStore, path: String): String = {
    preferenceStore.getString(PreferenceConstants.RUST_HOME) + path
  }

  def rustFileSearch(startPath: File, fileBuffer: ListBuffer[File], word: String): List[File] = {
    fileBuffer ++= startPath.listFiles.filter(f => !f.isDirectory && f.getName.endsWith(word + ".rs"))
    if (fileBuffer.nonEmpty) fileBuffer
    else
      startPath.listFiles.filter(_.isDirectory).foreach(d => rustFileSearch(d, fileBuffer, word))
    fileBuffer.toList
  }

  def findFiles(lib: String, path: String*): List[File] = {
    val fileBuffer: ListBuffer[File] = new ListBuffer
    path.foreach(l => rustFileSearch(new File(l), fileBuffer, lib))
    fileBuffer.toList
  }

  private def getTypedString(document: IDocument, offset: Int): String = {
    @tailrec
    def loop(dis: Int, c: Char, typedString: StringBuilder): String = {
      if (Character.isWhitespace(c)) typedString.reverse.toString()
      else
        loop(dis - 1, document.getChar(dis), typedString += c)
    }
    loop(offset - 2, document.getChar(offset - 1), new StringBuilder)
  }

  def getCompletionProposalAutoActivationCharacters: Array[Char] = Array[Char](':', ':')

  def getContextInformationAutoActivationCharacters = Array[Char](':', ':')

  def getErrorMessage = null

  def getContextInformationValidator = new RustParameterListParameter()

}

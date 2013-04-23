package org.baksia.rustycage.editors

import org.eclipse.jface.text.contentassist.{ CompletionProposal, ICompletionProposal, IContentAssistProcessor }
import org.eclipse.jface.text.{ IDocument, ITextViewer }
import scala.collection.mutable.ArrayBuffer
import org.eclipse.jface.text.contentassist.IContextInformation
import scala.annotation.tailrec
import org.baksia.rustycage.preferences.PreferenceConstants
import org.baksia.rustycage.RustPlugin
import java.io.File
import scala.collection.mutable.ListBuffer
import scala.io.Source
import org.eclipse.jface.text.contentassist.ContextInformation

class RustContentAssistProcessor extends IContentAssistProcessor {

  def computeContextInformation(viewer: ITextViewer, offset: Int): Array[IContextInformation] = {
    null
  }

  import SplitToTuple._
  override def computeCompletionProposals(viewer: ITextViewer, offset: Int): Array[ICompletionProposal] = {
    val document = viewer.getDocument
    val typedString = getTypedString(document, offset)

    //KeyWord completions
    val proposalBuffer = new ArrayBuffer[ICompletionProposal](RustParser.Keywords.length)
    if (typedString.contains("::")) {
      val libWord = typedString.splitToTuple("::")
      fetchLibProposals(typedString, proposalBuffer, offset, libWord)
    } else {
      RustParser.Keywords.foreach(keyword =>
        if(keyword.startsWith(typedString))
        proposalBuffer += new CompletionProposal(keyword.trim(), offset - typedString.length(), typedString.length(), keyword.length()))
    }

    proposalBuffer.toArray
  }
  
  private def fetchLibProposals(typedString: String, proposalsdBuffer: ArrayBuffer[ICompletionProposal], offset: Int, libWord: (String, String)) {
    val preferenceStore = RustPlugin.prefStore
    val rustPath = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libcore"
    val rustPathStd = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd"
    val word = libWord._2
    val lib = libWord._1
    findFiles(rustPath, rustPathStd).foreach(file =>
      Source.fromFile(file).getLines.foreach(line =>
        if (line.startsWith("fn") && line.contains(word)) {
          val token = line.splitToTuple("fn")._2
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
        }))
  }

  def rustFileSearch(startPath: File, fileBuffer: ListBuffer[File]): List[File] = {
    fileBuffer ++= startPath.listFiles.filter(!_.isDirectory()).filter(_.getName.endsWith(".rs"))
    startPath.listFiles.filter(_.isDirectory()).foreach(d => rustFileSearch(d, fileBuffer))
    fileBuffer.toList
  }

  def findFiles(path: String*): List[File] = {
    val fileBuffer: ListBuffer[File] = new ListBuffer
    path.foreach(l => rustFileSearch(new File(l), fileBuffer))
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

  //TODO : Need to activate from ::, look at CDT ?
  def getCompletionProposalAutoActivationCharacters: Array[Char] = Array[Char](':', ':')

  def getContextInformationAutoActivationCharacters = Array[Char](':', ':')

  def getErrorMessage = null

  def getContextInformationValidator = new RustParameterListParameter()

}

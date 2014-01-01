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

  def computeContextInformation(viewer: ITextViewer, offset: Int): Array[IContextInformation] = null

  import SplitToTuple._

  override def computeCompletionProposals(viewer: ITextViewer, offset: Int): Array[ICompletionProposal] = {
    val document = viewer.getDocument
    val typedString = getTypedString(document, offset)

    //KeyWord completions
    val proposalBuffer = new ArrayBuffer[ICompletionProposal]()
    if (typedString.contains("::")) {
      val libWord = typedString.splitToTuple("::")
      fetchLibProposals(typedString, proposalBuffer, offset, libWord)
    } else {
      //Add proposals from src/core/prelude
      val preferenceStore = RustPlugin.prefStore
      val preludePath = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd/prelude.rs"

      RustParser.Keywords.foreach(keyword =>
        if (keyword.startsWith(typedString))
          proposalBuffer += new CompletionProposal(keyword.trim(), offset - typedString.length(), typedString.length(), keyword.length()))

      Source.fromFile(preludePath, "UTF-8").getLines().toList.foreach(newLine => {
        val line = newLine.trim()
        
        if (line.startsWith("pub use") && line.contains(typedString) && !line.contains("test")) {
          val token = line.replace("pub use", "")
          if (token.contains("::")) {
            val props = token.split("::")(1).split(",")

            props.foreach(word => {
              if (word.startsWith(typedString)) {
                val proposal = word.replace("{", "").replace("}", "").replace(";", "")
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
    proposalBuffer.toArray
  }

  private def fetchLibProposals(typedString: String, proposalsdBuffer: ArrayBuffer[ICompletionProposal], offset: Int, libWord: (String, String)) {
    val preferenceStore = RustPlugin.prefStore
    val rustPath = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libextra"
    val rustPathStd = preferenceStore.getString(PreferenceConstants.P_PATH) + "/src/libstd"
    val word = libWord._2
    val lib = libWord._1
    val list = findFiles(lib, rustPath, rustPathStd)
    if (!list.isEmpty) {
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

  def rustFileSearch(startPath: File, fileBuffer: ListBuffer[File], word: String): List[File] = {
    fileBuffer ++= startPath.listFiles.filter(f => !f.isDirectory && f.getName.endsWith(word + ".rs"))
    if (!fileBuffer.isEmpty) fileBuffer
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

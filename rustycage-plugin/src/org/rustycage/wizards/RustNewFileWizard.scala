package org.rustycage.wizards

import org.rustycage.RustPlugin
import org.eclipse.core.resources._
import org.eclipse.core.runtime._
import org.eclipse.jface.operation.IRunnableWithProgress
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.wizard.Wizard
import org.eclipse.ui._
import org.eclipse.ui.ide.IDE

import java.io.{ByteArrayInputStream, InputStream}
import java.lang.reflect.InvocationTargetException

class RustNewFileWizard extends Wizard with INewWizard {
  setNeedsProgressMonitor(true)

  override def addPages() {
    pageFile = new RustNewFileWizardPage(selection)
    addPage(pageFile)
  }

  override def init(workbench: IWorkbench, selection: IStructuredSelection) {
    RustNewFileWizard.this.selection = selection
  }

  override def performFinish(): Boolean = {
    val containerName = pageFile.getContainerName
    val fileName = pageFile.getFileName
    def op = new IRunnableWithProgress() {
      def run(monitor: IProgressMonitor) {
        try {
          doFinish(containerName, fileName, monitor)
        } catch {
          case e: CoreException => throw new InvocationTargetException(e)
        } finally {
          monitor.done()
        }
      }
    }
    try {
      getContainer.run(true, false, op)
    } catch {
      case _: Exception =>
        false
    }
    true
  }


  def doFinish(containerName: String, fileName: String, monitor: IProgressMonitor) {
    monitor.beginTask("Creating " + fileName, 2)
    val root: IWorkspaceRoot = ResourcesPlugin.getWorkspace.getRoot

    val container: IContainer = root.findMember(new Path(containerName)).asInstanceOf[IContainer]

    if (!container.exists()) {
      throwCoreException(s"Container \'$containerName\' does not exist.")
    }
    
    val file = container.getFile(new Path(fileName))

    val crateFile = findCrate(container).asInstanceOf[IFile]

    val stream: InputStream = openContentStream()
    val crateStream: InputStream = openCrateContentStream(fileName)
    if (file.exists()) {
      file.setContents(stream, true, true, monitor)
    } else {
      file.create(stream, true, monitor)
    }
    if (crateFile.exists()) {
      crateFile.appendContents(crateStream, true, true, monitor)
    }
    crateStream.close()
    stream.close()

    monitor.worked(1)
    monitor.setTaskName("Opening file for editing...")
    getShell.getDisplay.asyncExec(new Runnable() {
      def run() {
        val page: IWorkbenchPage = PlatformUI.getWorkbench.getActiveWorkbenchWindow.getActivePage
        IDE.openEditor(page, file, true)
      }
    })
    monitor.worked(1)
  }

  private def openCrateContentStream(fileName: String): InputStream = {
    val modName = fileName.substring(0, fileName.length() - 3)
    val contents = s"\nmod $modName;"
    new ByteArrayInputStream(contents.getBytes)
  }

  //TODO: Remove isLib from prefStore. Ask in new file wizard for main function
  private def openContentStream(): InputStream = {
    if (RustPlugin.prefStore.getBoolean("IsLib")) new ByteArrayInputStream(TEMPLATE_LIB.getBytes)
    else
      new ByteArrayInputStream(TEMPLATE_MAIN.getBytes)
  }

  private def throwCoreException(message: String) = {
    val status =
      new Status(IStatus.ERROR, "RustyCage", IStatus.OK, message, null)
    throw new CoreException(status)
  }

  //Move this function out a trait CrateFinder or in RustPlugin ??
  def findCrate(dir: IContainer): IResource = {
    val crates = dir.members().filter(r => r.getFileExtension == "rc")
    if (crates.length > 1)
      throw new RuntimeException
    else
      crates(0)
  }

  private var pageFile: RustNewFileWizardPage = _

  private var selection: ISelection = _

  private val TEMPLATE_MAIN =
    """/******
      | * This file is generated with RustyCage
      | */
      |#[main]
      |fn run() {
      |  spawn(hello);
      |}
      |
      |fn hello() {
      |  println!("Hello world");
      |}
      |""".stripMargin

  private val TEMPLATE_LIB =
    """/******
      | * This file is generated with RustyCage
      | */
      | """.stripMargin

}
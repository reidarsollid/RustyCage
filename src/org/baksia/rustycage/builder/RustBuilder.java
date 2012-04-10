package org.baksia.rustycage.builder;

import org.baksia.rustycage.Activator;
import org.baksia.rustycage.compile.HackedRustCompiler;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import javax.xml.parsers.SAXParserFactory;
import java.util.Map;

public class RustBuilder extends IncrementalProjectBuilder {

    class SampleDeltaVisitor implements IResourceDeltaVisitor {

        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
                case IResourceDelta.ADDED:
                    // handle added resource
//                    resource.
//				checkXML(resource);
                    break;
                case IResourceDelta.REMOVED:
                    // handle removed resource
                    break;
                case IResourceDelta.CHANGED:
                    // handle changed resource
//				checkXML(resource);
                    break;
            }
            //return true to continue visiting children.
            return true;
        }
    }

    private class RustResourceVisitor implements IResourceVisitor, IResourceDeltaVisitor {


        @Override
        public boolean visit(IResourceDelta iResourceDelta) throws CoreException {
            switch (iResourceDelta.getKind()) {
                case IResourceDelta.ADDED:
                    return true;
                case IResourceDelta.REMOVED:
                    return true;
                case IResourceDelta.CHANGED:
                    return true;
                default:
                    return true;
            }
        }

        @Override
        public boolean visit(IResource iResource) throws CoreException {
            return true;
        }
    }

    /*class XMLErrorHandler extends DefaultHandler {

         private IFile file;

         public XMLErrorHandler(IFile file) {
             this.file = file;
         }

         private void addMarker(SAXParseException e, int severity) {
             RustBuilder.this.addMarker(file, e.getMessage(), e
                     .getLineNumber(), severity);
         }

         public void error(SAXParseException exception) throws SAXException {
             addMarker(exception, IMarker.SEVERITY_ERROR);
         }

         public void fatalError(SAXParseException exception) throws SAXException {
             addMarker(exception, IMarker.SEVERITY_ERROR);
         }

         public void warning(SAXParseException exception) throws SAXException {
             addMarker(exception, IMarker.SEVERITY_WARNING);
         }
     }*/

    public static final String BUILDER_ID = "RustyCage.rustBuilder";

    private static final String MARKER_TYPE = "org.eclipse.core.resources.problemmarker";

    private SAXParserFactory parserFactory;

    private void addMarker(IFile file, String message, int lineNumber,
                           int severity) {
        try {
            IMarker marker = file.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY, severity);
            if (lineNumber == -1) {
                lineNumber = 1;
            }
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
        } catch (CoreException e) {
            Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
        }
    }

    @Override
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
            throws CoreException {
        switch (kind) {
            case FULL_BUILD:
                fullBuild(monitor);
                break;
            case AUTO_BUILD:
            case CLEAN_BUILD:
                cleanBuild(monitor);
            case INCREMENTAL_BUILD:
                IResourceDelta delta = getDelta(getProject());
                incrementalBuild(delta, monitor);
            default:

            break;

        }
        return null;
    }

    private void cleanBuild(IProgressMonitor monitor) throws CoreException {
        clean(monitor);
        fullBuild(monitor);
    }

    private void rustCompile(IResource resource) {
      //  HackedRustCompiler.compile(resource.getFullPath().toFile())
    }

//	void checkXML(IResource resource) {
//		if (resource instanceof IFile && resource.getName().endsWith(".xml")) {
//			IFile file = (IFile) resource;
//			deleteMarkers(file);
//			XMLErrorHandler reporter = new XMLErrorHandler(file);
//			try {
//				getParser().parse(file.getContents(), reporter);
//			} catch (Exception e1) {
//			}
//		}
//	}

    private void deleteMarkers(IFile file) {
        try {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
        } catch (CoreException ce) {
        }
    }

    protected void fullBuild(final IProgressMonitor monitor)
            throws CoreException {
        try {
            getProject().accept(new RustResourceVisitor());
        } catch (CoreException e) {
        }
    }

    protected void incrementalBuild(IResourceDelta delta,
                                    IProgressMonitor monitor) throws CoreException {
        // the visitor does the work.
        delta.accept(new RustResourceVisitor());
    }
}

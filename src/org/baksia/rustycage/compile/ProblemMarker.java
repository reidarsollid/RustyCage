package org.baksia.rustycage.compile;


import org.baksia.rustycage.RustPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ProblemMarker {
	private static final String MARKER_TYPE = "org.eclipse.core.resources.problemmarker";

	void parseProblemFirstLine(String errorString, IFile file) {
		// TODO: Replace with regexp
		if (errorString.contains(".rs") && errorString.contains("error")) {
			// /home/reidar/runtime-EclipseApplication/RustProject/src/new_file.rs:4:1:
			// 4:8 error: unresolved name: println
			String tokens[] = errorString.split(":");
			String markerString = tokens[tokens.length - 1] + ": "
					+ tokens[tokens.length - 2];
			int lineNumber = 0;
			try {
				lineNumber = Integer.parseInt(tokens[1]);
			} catch (NumberFormatException exception) {
				// At least we get to mark the file with error
				lineNumber = 0;
			}
			addMarker(file, markerString, lineNumber, IMarker.SEVERITY_ERROR);
		}
	}

	public void parseProblemSecondLine(String errorString, IFile theFile) {
		if (errorString.contains(".rs")) {
			String tokens[] = errorString.split(":");
			String markerString = tokens[tokens.length - 1] + ": "
					+ tokens[tokens.length - 2];
			int lineNumber = 0;
			try {
				lineNumber = Integer.parseInt(tokens[1].replaceAll( "[^\\d]", "" ));
			} catch (NumberFormatException exception) {
				// At least we get to mark the file with error
				lineNumber = 0;
			}
			addMarker(theFile, markerString, lineNumber, IMarker.SEVERITY_ERROR);
		}		
	}

	void addMarker(IFile file, String message, int lineNumber, int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);

			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);

		} catch (CoreException e) {
			RustPlugin
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, RustPlugin.PLUGIN_ID(), e
							.getMessage()));
		}
	}

	void clearMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			RustPlugin
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, RustPlugin.PLUGIN_ID(), e
							.getMessage()));
		}
	}

}

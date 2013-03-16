package org.baksia.rustycage.compile;

import org.junit.Test;

public class ProblemMarkerTest {

	@Test
	public void testParseProblemFirstLine() {
		ProblemMarker marker = new ProblemMarker();
		String errorString = "/home/reidar/runtime-EclipseApplication/hello/src/area.rs:37:23: 37:30 error: unresolved name: Message\n" +
				"/home/reidar/runtime-EclipseApplication/hello/src/area.rs:37 	let port = comm::Port<Message>();\n"
				+"                                                                                    ^~~~~~~\n"+
				"error: aborting due to previous error";
		marker.parseProblemFirstLine(errorString, null);
	}

}

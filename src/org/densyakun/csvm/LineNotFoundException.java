package org.densyakun.csvm;
@SuppressWarnings("serial")
public class LineNotFoundException extends Throwable {
	LineNotFoundException(int l) {
		super("Line: " + l);
	}
}

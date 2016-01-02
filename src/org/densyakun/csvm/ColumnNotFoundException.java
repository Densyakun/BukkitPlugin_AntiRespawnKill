package org.densyakun.csvm;
@SuppressWarnings("serial")
public class ColumnNotFoundException extends Throwable {
	ColumnNotFoundException(int l, int c) {
		super("Line: " + l + " Column: " + c);
	}
}

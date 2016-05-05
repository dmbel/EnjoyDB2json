package ru.enjoy.server.exceptions;

@SuppressWarnings("serial")
public class BadDataAnnotationException extends Exception {

	public BadDataAnnotationException(String msg, Throwable e) {
		super(msg, e);
	}

	public BadDataAnnotationException(String msg) {
		super(msg);
	}

	public BadDataAnnotationException(SecurityException e) {
		super(e);
	}

}

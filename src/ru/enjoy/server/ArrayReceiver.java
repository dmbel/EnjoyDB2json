package ru.enjoy.server;

import ru.enjoy.server.exceptions.BadDataAnnotationException;

public interface ArrayReceiver {
	void putArray(String type, String[] vals) throws BadDataAnnotationException;
}

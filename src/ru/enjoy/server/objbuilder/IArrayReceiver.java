package ru.enjoy.server.objbuilder;

import ru.enjoy.server.data.specificator.DataObject;
import ru.enjoy.server.exceptions.BadDataAnnotationException;

public interface IArrayReceiver {
	void putArray(String type, String[] vals) throws BadDataAnnotationException;
}

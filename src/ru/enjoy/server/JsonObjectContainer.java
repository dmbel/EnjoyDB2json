package ru.enjoy.server;

import java.util.HashMap;
import java.util.Map;

import ru.enjoy.server.data.*;

public class JsonObjectContainer {
	public static final String[] DATA_CLASS_LIST = {
		"ru.enjoy.server.data.Category",
		"ru.enjoy.server.data.Product",
		"ru.enjoy.server.data.ProductType"
	};
	public	Root json;
	void putObject(Object obj){
		
	}
	Object getObject(String className, int id){
		return null;
	};
}

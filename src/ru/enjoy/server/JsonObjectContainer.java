package ru.enjoy.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.data.*;

public class JsonObjectContainer {
	public static final String[] DATA_CLASS_LIST = { "ru.enjoy.server.data.Category", "ru.enjoy.server.data.Product",
			"ru.enjoy.server.data.ProductType" };
	public Root root = new Root();
	public Map<String, List> class2list = new HashMap();
	
	public JsonObjectContainer() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		super();
		// Делаем доступ к спискам объектов в Root не только по именам списков,
		// но и по имени класса объектов списков
		for (int i = 0; i < DATA_CLASS_LIST.length; i++) {
			Class<?> cls = Class.forName(DATA_CLASS_LIST[i]);
			DBTable an = cls.getAnnotation(DBTable.class);
			if(an == null) continue;
			Field listFld = root.getClass().getDeclaredField(an.JsonListName());
			class2list.put(DATA_CLASS_LIST[i],(List)(listFld.get(root)));
		}
	}

	void putObject(Object obj) {
		// Помещаем объект в список соответствующий по аннотации JsonListName
		List list = class2list.get(obj.getClass().getName());
		list.add(obj);
	}
}

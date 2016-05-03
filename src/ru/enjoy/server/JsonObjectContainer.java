package ru.enjoy.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.Exeptions.DataFieldNoExistsExeption;
import ru.enjoy.server.Exeptions.NoDataClassExeption;
import ru.enjoy.server.data.*;
import static ru.enjoy.server.Exeptions.*;

public class JsonObjectContainer {
	public static final String[] DATA_CLASS_LIST = { "ru.enjoy.server.data.Category", "ru.enjoy.server.data.Product",
			"ru.enjoy.server.data.ProductType" };
	public Root root = new Root();
	// По имени класса хранит ссылку на список эелементов этого класса
	public Map<String, List<Object>> class2list = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public JsonObjectContainer() throws BadDataAnnotationExeption{
		super();
		// Заполняем class2list
		for (int i = 0; i < DATA_CLASS_LIST.length; i++) {
			Class<?> cls;
			try {
				cls = Class.forName(DATA_CLASS_LIST[i]);
			} catch (ClassNotFoundException e) {
				throw new NoDataClassExeption(DATA_CLASS_LIST[i], e);
			}
			DBTable an = cls.getAnnotation(DBTable.class);
			if(an == null) continue;
			try {
				Field listFld = root.getClass().getDeclaredField(an.JsonListName());
				class2list.put(DATA_CLASS_LIST[i],((List<Object>)(listFld.get(root))));
			} catch (IllegalAccessException e) {
				throw new DataFieldAccessExeption(root.getClass().getName(), an.JsonListName(), e);
			} catch (NoSuchFieldException e) {
				throw new DataFieldNoExistsExeption(root.getClass().getName(), an.JsonListName(), e);
			}
		}
	}

	void putObject(Object obj) {
		// Помещаем объект в список соответствующий по аннотации JsonListName
		List<Object> list = class2list.get(obj.getClass().getName());
		list.add(obj);
	}
}

package ru.enjoy.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.Exeptions.BadDataAnnotationExeption;
import ru.enjoy.server.Exeptions.DataFieldNoExistsExeption;
import ru.enjoy.server.Exeptions.DataNoUniqIdExeption;
import ru.enjoy.server.Exeptions.NoDataClassExeption;
import ru.enjoy.server.data.*;
import static ru.enjoy.server.Exeptions.*;

public class JsonObjectContainer {
	public static final String[] DATA_CLASS_LIST = { 
			"ru.enjoy.server.data.Category", 
			"ru.enjoy.server.data.Product",
			"ru.enjoy.server.data.ProductType",
			"ru.enjoy.server.data.ProductVariant",
			"ru.enjoy.server.data.ProductAndCategoryPointer"};
	public Root root = new Root();
	// По имени класса хранит ссылку на список эелементов этого класса
	private Map<String, List<Object>> class2list = new HashMap<>();
	// Индексы для поиска записи по имени класса и значению поля 
	private Map<String, Map<Integer, Object>> indexes = new HashMap<>();
	
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

	public void putObject(Object obj) {
		// Помещаем объект в список соответствующий по аннотации JsonListName
		List<Object> list = class2list.get(obj.getClass().getName());
		list.add(obj);
	}
	
	// Встраиваем дочерние объекты в списки родительских
	public void makeChilds() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, BadDataAnnotationExeption{
		for(String clsName : DATA_CLASS_LIST){
			Class<Object> cls = (Class<Object>) Class.forName(clsName);
			ChildEntity chEnt =  cls.getAnnotation(ChildEntity.class);
			if(chEnt != null){
				Field listFld = Class.forName(chEnt.parentClass()).getDeclaredField(chEnt.parentListField());
				for(Object child : class2list.get(clsName)){
					int keyVal = child.getClass().getDeclaredField(chEnt.referenceField()).getInt(child);
					Object parent = getObject(chEnt.parentClass(), chEnt.parentIdField(), keyVal);
					List<Object> lst = (List<Object>) listFld.get(parent);
					lst.add(child);
				}
			}
		}
	}

	public Object getObject(String className, String fieldName, int fieldVal) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, BadDataAnnotationExeption {
		// Если индекса еще нет, то индексируем 
		// (при первом обращений по данному сочетанию className и fieldName)
		String indexIndex = className + "_" + fieldName; 
		if(indexes.get(indexIndex) == null){
			Map<Integer, Object> index = new HashMap<>();
			Field fld = Class.forName(className).getDeclaredField(fieldName);
			for(Object obj : class2list.get(className)){
				if(index.put(fld.getInt(obj), obj)!=null){
					// Значение поля не уникально
					throw new DataNoUniqIdExeption(className, fieldName, fieldVal);
				}
			}
			indexes.put(indexIndex, index);
		}
		return indexes.get(indexIndex).get(fieldVal);
	}
}

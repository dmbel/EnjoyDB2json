package ru.enjoy.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.data.*;
import ru.enjoy.server.exceptions.BadDataAnnotationException;
import ru.enjoy.server.exceptions.Exeptions.BadDataAnnotationExeption;
import ru.enjoy.server.exceptions.Exeptions.DataFieldNoExistsExeption;
import ru.enjoy.server.exceptions.Exeptions.DataNoUniqIdExeption;
import ru.enjoy.server.exceptions.Exeptions.NoDataClassExeption;
import static ru.enjoy.server.exceptions.Exeptions.*;

@SuppressWarnings("rawtypes")
public class JsonObjectContainer implements ArrayReceiver {
	public static final String[] DATA_CLASS_LIST = { "ru.enjoy.server.data.Category", "ru.enjoy.server.data.Product",
			"ru.enjoy.server.data.ProductType", "ru.enjoy.server.data.ProductVariant",
			"ru.enjoy.server.data.ProductAndCategoryPointer" };
	// Ссылка на корень json объекта
	public Root root = new Root();
	// Индексы для поиска записи по имени класса и значению поля
	private Map<String, Map<Integer, Object>> indexes = new HashMap<>();

	private Map<String, Holder> className2Holder = new HashMap<>();
	private Map<String, String> tableName2ClassName = new HashMap<>();

	private void createClassName2Holder() throws BadDataAnnotationException {
		for (String className : DATA_CLASS_LIST) {
			Holder h = new Holder();
			try {
				h.cls = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new BadDataAnnotationException(String.format("Class %s does not exist", className), e);
			}
			DBTable an = (DBTable) h.cls.getAnnotation(DBTable.class);
			if (an == null)
				throw new BadDataAnnotationException(String.format("Class %s has not DBTable annotation", className));
			tableName2ClassName.put(an.TableName(), className);
			h.columnOrder = an.ColumnOrder().split(",");
			h.childEntity = (ChildEntity) h.cls.getAnnotation(ChildEntity.class);
			try {
				h.list = (List<Object>) root.getClass().getDeclaredField(an.JsonListName()).get(root);
			} catch (NoSuchFieldException e) {
				throw new BadDataAnnotationException(String.format(
						"Class %s in annotation DBTable has reference to field %s of % class? but such field does not exist.",
						className, an.JsonListName(), root.getClass().getName()), e);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				throw new BadDataAnnotationException(String.format("Field %s in class %s does not accessable.",
						an.JsonListName(), root.getClass().getName()), e);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			className2Holder.put(className, h);
		}
	}

	public JsonObjectContainer() throws BadDataAnnotationException {
		createClassName2Holder();
	}

	// Встраиваем дочерние объекты в списки родительских
	public void makeChilds() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, BadDataAnnotationExeption {

		for (String clsName : DATA_CLASS_LIST) {
			Holder h = className2Holder.get(clsName);
			if (h.childEntity == null)
				continue;
			String parentClsName = h.childEntity.parentClass();
			Field listFld = className2Holder.get(parentClsName).cls
					.getDeclaredField(h.childEntity.parentListField());
			for (Object child : h.list) {
				int keyVal = child.getClass().getDeclaredField(h.childEntity.referenceField()).getInt(child);
				Object parent = getObject(parentClsName, h.childEntity.parentIdField(), keyVal);
				Object val = listFld.get(parent);
				if(val instanceof List){
					List lst = (List)val;
				}
				lst.add(child);

			}
		}
	}

	public Object getObject(String className, String fieldName, int fieldVal)
			throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, BadDataAnnotationExeption {
		// Если индекса еще нет, то индексируем
		// (при первом обращений по данному сочетанию className и fieldName)
		String indexIndex = className + "_" + fieldName;
		Holder h = className2Holder.get(className);
		if (indexes.get(indexIndex) == null) {
			Map<Integer, Object> index = new HashMap<>();
			Field fld = Class.forName(className).getDeclaredField(fieldName);
			for (Object obj : h.list) {
				if (index.put(fld.getInt(obj), obj) != null) {
					// Значение поля не уникально
					throw new DataNoUniqIdExeption(className, fieldName, fieldVal);
				}
			}
			indexes.put(indexIndex, index);
		}
		return indexes.get(indexIndex).get(fieldVal);
	}

	@Override
	public void putArray(String[] vals) throws BadDataAnnotationException {
		Holder h = className2Holder.get(tableName2ClassName.get(vals[0]));
		int cnt = (vals.length < h.columnOrder.length) ? vals.length : h.columnOrder.length;
		Object obj;
		try {
			obj = h.cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BadDataAnnotationException("", e);
		}
		for (int i = 0; i < cnt; i++) {
			String fldName = h.columnOrder[i];
			if (!fldName.isEmpty()) {
				Field f = null;
				try {
					f = h.cls.getDeclaredField(fldName);
				} catch (NoSuchFieldException e) {
					throw new BadDataAnnotationException("", e);
				}
				try {
					if (f.getType().toString().equals("int")) {
						f.setInt(obj, Integer.parseInt(vals[i]));
					} else {
						f.set(obj, vals[i]);
					}
				} catch (IllegalAccessException e) {
					throw new BadDataAnnotationException("", e);
				} catch (NumberFormatException e) {
					throw new BadDataAnnotationException("", e);
				}

			}
		}
		h.list.add(obj);
	}

	/**
	 * @author dm
	 *
	 */
	private static class Holder {
		public Class cls;
		public String[] columnOrder;
		public List<Object> list;
		public ChildEntity childEntity;
	}
}

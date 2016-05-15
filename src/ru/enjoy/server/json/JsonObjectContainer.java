package ru.enjoy.server.json;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.ArrayReceiver;
import ru.enjoy.server.exceptions.BadDataAnnotationException;

@SuppressWarnings("rawtypes")
public class JsonObjectContainer implements ArrayReceiver {
	public static final String[] DATA_CLASS_LIST = {
			"ru.enjoy.server.data.Category", "ru.enjoy.server.data.Product",
			"ru.enjoy.server.data.ProductType",
			"ru.enjoy.server.data.ProductVariant",
			"ru.enjoy.server.data.ProductAndCategoryPointer" };
	// Ссылка на корень json объекта
	public Object root;
	// Индексы для поиска записи по имени класса и значению поля
	private Map<String, Map<Integer, Object>> indexes = new HashMap<>();

	private Map<String, Holder> className2Holder = new HashMap<>();
	private Map<String, Holder> tableName2Holder = new HashMap<>();

	/**
	 * Подготовка метаданных согласно списку классов данных
	 * 
	 * @throws BadDataAnnotationException
	 */
	private void prepareMetaData() throws BadDataAnnotationException {
		for (String className : DATA_CLASS_LIST) {
			Holder h = new Holder();
			try {
				h.cls = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new BadDataAnnotationException(String.format(
						"Class '%s' does not exist", className), e);
			}
			DBTable an = (DBTable) h.cls.getAnnotation(DBTable.class);
			if (an == null)
				throw new BadDataAnnotationException(String.format(
						"Class '%s' has not DBTable annotation", className));
			h.columnOrder = an.ColumnOrder().split(",");
			h.childEntity = (ChildEntity) h.cls
					.getAnnotation(ChildEntity.class);
			String addMsg = String
					.format("Field '%s' used as list for objects of class '%s' due to JsonListName attribute of DBTable annotation",
							an.JsonListName(), className);
			h.list = (List<Object>) get(root,
					getField(root.getClass(), an.JsonListName(), addMsg),
					addMsg);
			className2Holder.put(className, h);
			tableName2Holder.put(an.TableName(), h);
		}
	}

	public JsonObjectContainer(Object root) throws BadDataAnnotationException {
		this.root = root;
		prepareMetaData();
	}

	// Встраиваем дочерние объекты в списки родительских
	public void makeChilds() throws BadDataAnnotationException {

		for (String childClsName : DATA_CLASS_LIST) {
			Holder holderChild = className2Holder.get(childClsName);
			if (holderChild.childEntity == null)
				continue;
			Holder holderParent = className2Holder.get(holderChild.childEntity
					.parentClass());

			// Довесок к сообщениям об отсутствии поля в классе
			String exAddMsg = String.format(
					"It referenced from childEntity annotation of class '%s'.",
					childClsName);
			// Метаописание спискового поля родительского класса куда включать
			// дочерние
			Field listFld = getField(holderParent.cls,
					holderChild.childEntity.parentListField(), exAddMsg);

			// Метаописание id поля родительского класса по которому на него
			// ссылается дочерний
			Field parentKeyFld = getField(holderParent.cls,
					holderChild.childEntity.parentIdField(), exAddMsg);

			// Метаописание ссылочного поля дочернего класса класса которым он
			// ссылается на родительский
			Field childRefFld = getField(holderChild.cls,
					holderChild.childEntity.referenceField(), exAddMsg);

			// Перебираем все дочерние объекты и раскидываем их по родителям
			// согласно ссылке referenceField дочернего на parentIdField
			// родительского
			String addMsg = String
					.format("Field used as list for child objects of class '%s' (due to childEntity annotation).",
							childClsName);
			for (Object child : holderChild.list) {
				int keyVal = getInt(child, childRefFld,
						"Field used as integer parent reference due to childEntity annotation");
				Object parent = getObject(holderParent, parentKeyFld, keyVal);
				List lst = (List) get(parent, listFld, addMsg);
				lst.add(child);

			}
		}
	}

	private int getInt(Object obj, Field field, String addMsg)
			throws BadDataAnnotationException {
		try {
			return field.getInt(obj);
		} catch (IllegalArgumentException e) {
			throw new BadDataAnnotationException(String.format(
					"It seems field '%s' of class '%s' is not 'int'. ",
					field.getName(), obj.getClass().getName())
					+ addMsg, e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new BadDataAnnotationException(String.format(
					"It seems field '%s' of class '%s' is not 'public'. ",
					field.getName(), obj.getClass().getName())
					+ addMsg, e);
		}
	}

	private Object get(Object obj, Field field, String addMsg)
			throws BadDataAnnotationException {
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new BadDataAnnotationException(String.format(
					"It seems field '%s' of class '%s' is not 'public'. ",
					field.getName(), obj.getClass().getName())
					+ addMsg, e);
		}
	}

	/**
	 * Возвращает метаописание поля класса, а исключения заворачивает в
	 * BadDataAnnotationException
	 * 
	 * @param cls
	 * @param fieldName
	 * @param exMsg
	 * @return
	 * @throws BadDataAnnotationException
	 */
	private Field getField(Class cls, String fieldName, String addMsg)
			throws BadDataAnnotationException {
		try {
			return cls.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			throw new BadDataAnnotationException(String.format(
					"Field '%s' does not exist in class '%s'. ", fieldName,
					cls.getName())
					+ addMsg, e);
		}
	}

	private Object getObject(Holder holder, Field keyFld, int keyVal)
			throws BadDataAnnotationException {
		// Если индекса еще нет, то индексируем
		// (при первом обращений по данному сочетанию className и fieldName)
		String indexIndex = holder.cls.getName() + "_" + keyFld.getName();
		if (indexes.get(indexIndex) == null) {
			String addMsg = String
					.format("An error occurred while indexing class '%s' by field '%s'.",
							holder.cls.getName(), keyFld.getName());
			Map<Integer, Object> index = new HashMap<>();
			for (Object obj : holder.list) {
				if (index.put(getInt(obj, keyFld, addMsg), obj) != null) {
					// Значение поля не уникально
					throw new BadDataAnnotationException(
							String.format(
									"Field '%s' of class '%s' used as key, but it is not unical. (value=%d)",
									keyFld.getName(), holder.cls.getName(),
									keyVal));
				}
			}
			indexes.put(indexIndex, index);
		}
		return indexes.get(indexIndex).get(keyVal);
	}

	@Override
	public void putArray(String type, String[] vals) throws BadDataAnnotationException {
		Holder h = tableName2Holder.get(type);
		if (h == null)
			return;
		int cnt = (vals.length < h.columnOrder.length) ? vals.length
				: h.columnOrder.length;
		Object obj;
		try {
			obj = h.cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BadDataAnnotationException(
					"Error occured while putting new DB record to object of class "
							+ h.cls.getName() + ". " + e.getMessage(), e);
		}
		for (int i = 0; i < cnt; i++) {
			String fldName = h.columnOrder[i];
			if (!fldName.isEmpty()) {
				Field f = getField(h.cls, fldName,
						"This name used in 'ColumnOrder' attribute of 'DBTable' annotation.");
				try {
					if (f.getType().toString().equals("int")) {
						f.setInt(obj, Integer.parseInt(vals[i]));
					} else {
						f.set(obj, vals[i]);
					}
				} catch (IllegalAccessException e) {
					String msg = String.format("It seems field '%s' of class '%s' is not 'public'. ",
							fldName, h.cls.getName());
					throw new BadDataAnnotationException(msg, e);
				} catch (NumberFormatException e) {
					String msg = String.format("Value '%s' can not be parsed as 'int', but field '%s' of class '%s' has 'int' type", vals[i], fldName, h.cls.getName());
					throw new BadDataAnnotationException(msg, e);
				}

			}
		}
		h.list.add(obj);
	}

	/**
	 * Вспомогательный класс для объединения в один объект набора метаинформации
	 * о json классах. Чтобы не обращаться по несколько раз к reflection методам
	 * 
	 * @author dm
	 *
	 */
	private static class Holder {
		// Ссылка на метаописание класса
		public Class cls;
		// Список полей класса в том порядке в котором соответствующие колонки
		// таблицы
		// считываются из базы
		public String[] columnOrder;
		// Ссылка на список в который заносить объекты данного класса при чтении
		// из базы
		public List<Object> list;
		// Ссылка на аннотацию ChildEntity либо null если ее нет
		public ChildEntity childEntity;
	}
}

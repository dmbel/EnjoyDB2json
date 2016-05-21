/**
 * Класс для создания сложных объектов с данными из простых массивов строковых данных,
 * представляющих собой выгрузку из реляционной базы данных.
 * Инфрмацию о требуемой структуре итогового объекта берет из аннотации пакета ru.enjoy.server.data.specificator
 * 
 * Последовательность работы:
 * 1 - Создаем ObjectBuilder передавая в конструкор параметром ссылку на корневой объект структуры,
 * которая будет результирующим сложным объектом.
 * При этом консруктор получает из аннотаций информацию о структуре будующего сложного объекта данных.
 * 2 - Отдаем созданный ObjectBuilder на правах экземпляра IArrayReceiver в ту процедуру, 
 * которая будет наполнять его данными, путем многократного вызова putArray.
 * 3 - После завершения наполнения данными вызываем makeStruct. Этот метод формирует структуру объекта,
 *  расставляя вложенные объекты в соответствующие родительские.
 */
package ru.enjoy.server.objbuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.data.specificator.ChildList;
import ru.enjoy.server.data.specificator.DataObject;
import ru.enjoy.server.data.specificator.TableList;
import ru.enjoy.server.exceptions.BadDataAnnotationException;

public class ObjectBuilder implements IArrayReceiver {

	private Object root;
	private Map<String, Holder> className2Holder = new HashMap<>();
	private Map<String, Holder> tableName2Holder = new HashMap<>();

	/**
	 * Сохранить одну запись из базы в ObjectBuilder
	 * 
	 * @param type
	 *            имя таблицы в базе
	 * @param vals
	 *            массив строковых значений
	 * @throws BadDataAnnotationException
	 */
	@Override
	public void putArray(String type, String[] vals)
			throws BadDataAnnotationException {
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
						"This name used in 'fieldsOrder' attribute of 'DataObject' annotation.");
				String fType = null;
				try {
					fType = f.getType().getCanonicalName();
					switch (fType) {
					case "int":
						f.setInt(
								obj,
								vals[i].isEmpty() ? 0 : Integer
										.parseInt(vals[i]));
						break;
					case "long":
						f.setLong(obj,
								vals[i].isEmpty() ? 0 : Long.parseLong(vals[i]));
						break;
					case "double":
						f.setDouble(obj, Double.parseDouble(vals[i]));
						break;
					case "java.lang.String":
						f.set(obj, vals[i]);
						break;
					}
				} catch (IllegalAccessException e) {
					String msg = String
							.format("It seems field '%s' of class '%s' is not 'public'. ",
									fldName, h.cls.getName());
					throw new BadDataAnnotationException(msg, e);
				} catch (NumberFormatException e) {
					String msg = String
							.format("Value '%s' can not be parsed as '%s' value for '%s' field of class '%s'.",
									vals[i], fType, fldName, h.cls.getName());
					throw new BadDataAnnotationException(msg, e);
				}

			}
		}
		h.list.add(obj);

	}

	private Field getField(Class<?> cls, String fieldName, String addMsg)
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

	public ObjectBuilder(Object root) throws BadDataAnnotationException {
		this.root = root;
		registerClass(root.getClass());
		tableName2Holder.get("ROOT").list.add(root);
	}

	private void registerChilds(Class<?> cls) throws BadDataAnnotationException {
		for (Field field : cls.getFields()) {
			ChildList chList = field.getAnnotation(ChildList.class);
			TableList tableList = field.getAnnotation(TableList.class);
			String clsName = null;

			if (chList != null)
				clsName = chList.childClass();
			if (tableList != null)
				clsName = tableList.cls();
			if (clsName != null) {
				try {
					registerClass(Class.forName(clsName));
				} catch (ClassNotFoundException e) {
					throw new BadDataAnnotationException(
							String.format(
									"Class name '%s' used in annotation inside '%s', but such class does not exist in classpath.",
									clsName, cls.getName()), e);
				}
			}
		}
	}

	private void registerClass(Class<?> cls) throws BadDataAnnotationException {
		DataObject daoAnn = (DataObject) (cls.getAnnotation(DataObject.class));
		Holder h = new Holder();
		h.cls = cls;
		h.list = new ArrayList<>();
		h.columnOrder = daoAnn.fieldsOrder();
		className2Holder.put(cls.getCanonicalName(), h);
		tableName2Holder.put(daoAnn.typeCode(), h);
		registerChilds(cls);
	}

	private static class Holder {
		public Class<?> cls;
		public String[] columnOrder;
		public List<Object> list;
		boolean makedStruct = false;
	}

	public Object getRoot() {
		return root;
	}

	public void makeStruct() throws BadDataAnnotationException {
		makeStructInternal(root.getClass());
	}

	@SuppressWarnings("unchecked")
	private void makeStructInternal(Class<?> cls)
			throws BadDataAnnotationException {
		Holder h = className2Holder.get(cls.getCanonicalName());
		if (h.makedStruct)
			return;
		h.makedStruct = true;
		for (Field field : cls.getFields()) {
			TableList tableList = field.getAnnotation(TableList.class);
			if (tableList != null) {
				Holder chHolder = className2Holder.get(tableList.cls());
				for (Object parent : h.list) {
					for (Object child : chHolder.list) {
						List<Object> lst = getCreateList(parent, field, "Field annotated by TableList");
						lst.add(child);
					}
				}
				makeStructInternal(chHolder.cls);
			}
			ChildList chList = field.getAnnotation(ChildList.class);
			if (chList != null) {
				Holder chHolder = className2Holder.get(chList.childClass());
				String[] idFields = chList.idField();
				Field[] idFieldFlds = new Field[idFields.length];
				for (int i = 0; i < idFields.length; i++) {
					idFieldFlds[i] = getField(h.cls, idFields[i],
							"Field used in idFields attribute of annotation ChildList");
				}
				String[] refFields = chList.refField();
				Field[] refFieldFlds = new Field[refFields.length];
				for (int i = 0; i < idFields.length; i++) {
					refFieldFlds[i] = getField(chHolder.cls, refFields[i],
							"Field used in refFields attribute of annotation ChildList in class "
									+ cls.getName());
				}
				Map<ComplexKey, Object> idIndex = new HashMap<>();
				for (Object parent : h.list) {
					Object[] key = new Object[idFields.length];
					for (int i = 0; i < idFields.length; i++) {
						key[i] = get(parent, idFieldFlds[i],
								"Field used in idFields attribute of annotation ChildList");
					}
					idIndex.put(new ComplexKey(key), parent);
				}
				for (Object child : chHolder.list) {
					Object[] key = new Object[refFields.length];
					for (int i = 0; i < refFields.length; i++) {
						key[i] = get(child, refFieldFlds[i],
								"Field used in refFields attribute of annotation ChildList in class "
										+ cls.getName());
					}
					Object parent = idIndex.get(new ComplexKey(key));
					if (parent != null) {
						List<Object> lst = getCreateList(parent, field,
								"Field annotated by ChildList");
						lst.add(child);
					}
				}
				makeStructInternal(chHolder.cls);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private List getCreateList(Object obj, Field field, String addMsg)
			throws BadDataAnnotationException {
		List<?> list = (List<?>) get(obj, field, addMsg);
		if (list == null) {
			list = new ArrayList<>();
			try {
				field.set(obj, list);
			} catch (IllegalAccessException e) {
				throw new BadDataAnnotationException(String.format(
						"It seems field '%s' of class '%s' is not 'public'. ",
						field.getName(), obj.getClass().getName())
						+ addMsg, e);
			}
		}
		return list;
	}

	private Object get(Object obj, Field field, String addMsg)
			throws BadDataAnnotationException {
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			throw new BadDataAnnotationException(String.format(
					"It seems field '%s' of class '%s' is not 'public'. ",
					field.getName(), obj.getClass().getName())
					+ addMsg, e);
		}
	}

	/**
	 * Вспомогательный класс для составного ключа (массив строк и boxed простых
	 * типов) Он нужен для использования в качестве ключа в HashMap, а значит
	 * ему нужно переопределить equals и hashCode таким образом чтобы они
	 * зависели от всех входящих в коллекцию ключа полей
	 * 
	 * @author dm
	 *
	 */
	private static class ComplexKey {
		Object[] keyVals;

		ComplexKey(Object[] keyVals) {
			this.keyVals = keyVals;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ComplexKey))
				return false;
			ComplexKey ck = (ComplexKey) o;
			if (keyVals.length != ck.keyVals.length)
				return false;
			for (int i = 0; i < keyVals.length; i++) {
				if (!keyVals[i].equals(ck.keyVals[i]))
					return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int res = 0;
			for (int i = 0; i < keyVals.length; i++) {
				res += keyVals[i].hashCode();
			}
			return res;
		}
	}
}

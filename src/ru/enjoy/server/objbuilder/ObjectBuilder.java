package ru.enjoy.server.objbuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.enjoy.server.data.specificator.ChildList;
import ru.enjoy.server.data.specificator.DataObject;
import ru.enjoy.server.exceptions.BadDataAnnotationException;

public class ObjectBuilder implements IArrayReceiver {

	private Object root;
	private Map<String, Holder> className2Holder = new HashMap<>();
	private Map<String, Holder> tableName2Holder = new HashMap<>();

	@Override
	public void putArray(String type, String[] vals) throws BadDataAnnotationException {
		Holder h = tableName2Holder.get(type);
		if (h == null)
			return;
		int cnt = (vals.length < h.columnOrder.length) ? vals.length : h.columnOrder.length;
		Object obj;
		try {
			obj = h.cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BadDataAnnotationException("Error occured while putting new DB record to object of class "
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
						f.setInt(obj, Integer.parseInt(vals[i]));
						break;
					case "long":
						f.setLong(obj, Long.parseLong(vals[i]));
						break;
					case "double":
						f.setDouble(obj, Double.parseDouble(vals[i]));
						break;
					case "java.lang.String":
						f.set(obj, vals[i]);
						break;
					}
				} catch (IllegalAccessException e) {
					String msg = String.format("It seems field '%s' of class '%s' is not 'public'. ", fldName,
							h.cls.getName());
					throw new BadDataAnnotationException(msg, e);
				} catch (NumberFormatException e) {
					String msg = String.format("Value '%s' can not be parsed as '%s'.", vals[i], fldName, fType);
					throw new BadDataAnnotationException(msg, e);
				}

			}
		}
		h.list.add(obj);

	}

	private Field getField(Class cls, String fieldName, String addMsg) throws BadDataAnnotationException {
		try {
			return cls.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			throw new BadDataAnnotationException(
					String.format("Field '%s' does not exist in class '%s'. ", fieldName, cls.getName()) + addMsg, e);
		}
	}

	public ObjectBuilder(Object root) throws ClassNotFoundException {
		this.root = root;
		Holder h = new Holder();
		h.list = Arrays.asList(root);
		h.cls = root.getClass();
		className2Holder.put(h.cls.getCanonicalName(), h);
		tableName2Holder.put("", h);
		registerChilds(h.cls);
	}

	private void registerChilds(Class<?> cls) throws ClassNotFoundException {
		for (Field field : cls.getFields()) {
			ChildList chList = field.getAnnotation(ChildList.class);
			if (chList != null) 
				registerClass(Class.forName(chList.childClass()));
		}
	}

	private void registerClass(Class<?> cls) throws ClassNotFoundException {
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
	}

	public Object getRoot() {
		return root;
	}

	public void makeStruct() throws BadDataAnnotationException {
		makeStructInternal(root.getClass());
	}

	private void makeStructInternal(Class<?> cls) throws BadDataAnnotationException {
		Holder h = className2Holder.get(cls.getCanonicalName());
		for (Field field : cls.getFields()) {
			ChildList chList = field.getAnnotation(ChildList.class);
			if (chList == null)
				continue;
			Holder chHolder = className2Holder.get(chList.childClass());
			String idField = chList.idField();
			if (idField.isEmpty()) {
				for (Object parent : h.list) {
					for (Object child : chHolder.list) {
						List<Object> lst = (List<Object>) get(parent, field, "");
						lst.add(child);
					}
				}
			} else {
				Field idFieldFld = getField(h.cls, idField, "");
				Field refFieldFld = getField(chHolder.cls, chList.refField(), "");
				Map<Object, Object> idIndex = new HashMap<>();
				for (Object parent : h.list) {
					idIndex.put(get(parent, idFieldFld, ""), parent);
				}
				for (Object child : chHolder.list) {
					Object val = get(child, refFieldFld, "");
					Object parent = idIndex.get(val);
					if (parent != null) {
						List<Object> lst = (List<Object>) get(parent, field, "");
						lst.add(child);
					}
				}
			}
			makeStructInternal(chHolder.cls);
		}
	}

	private Object get(Object obj, Field field, String addMsg) throws BadDataAnnotationException {
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new BadDataAnnotationException(String.format("It seems field '%s' of class '%s' is not 'public'. ",
					field.getName(), obj.getClass().getName()) + addMsg, e);
		}
	}
}

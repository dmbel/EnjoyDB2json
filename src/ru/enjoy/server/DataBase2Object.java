package ru.enjoy.server;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import ru.enjoy.server.data.Product;

import javax.xml.parsers.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Загрузчик данных из файла базы данных в объекты
 * 
 * @author dm
 *
 */
public class DataBase2Object {
	private static final int MAX_FLDS_NUM = 11; // количество тегов внутри записи
	private static final String DB_RECORD_TAG = "p"; // Тег обрамляющий запись
	private static final String DB_FIELD_TAG = "span"; // Тег обрамляющий поле

	// Карта соотвествия имени таблицы классу объекта 
	private Map<String, Class> table2objectClassMap = new HashMap<>();
	// Имя таблицы - Порядок полей
	private Map<String, String[]> table2columnOrder = new HashMap<>();

	public void load(String fileName, JsonObjectContainer joc) throws ParserConfigurationException,
			FileNotFoundException, SAXException, IOException,
			NumberFormatException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		// ObjectDataBase objectDataBase = new ObjectDataBase();
		Document doc = builder.parse(new FileInputStream(fileName));
		NodeList pList = doc.getElementsByTagName(DB_RECORD_TAG);
		for (int i = 0; i < pList.getLength(); i++) {
			String[] vals = getValsFromPTag(pList.item(i));
			Class cls = table2objectClassMap.get(vals[0]);
			if(cls==null) continue;
			Object rec = cls.newInstance();
			setObjFlds(rec, vals, table2columnOrder.get(vals[0]));
			joc.putObject(rec);
		}
	}

	/**
	 * Вытащить массив из MAX_FLDS_NUM строковых значений, лежащих во вложенном span
	 * 
	 * @param node
	 * @return
	 */
	private String[] getValsFromPTag(Node node) {
		String[] vals = new String[MAX_FLDS_NUM];
		NodeList childs = node.getChildNodes();
		for (int i = 0, k = 0; ((i < childs.getLength())&&(k < MAX_FLDS_NUM)); i++) {
			if (childs.item(i).getNodeName().equals(DB_FIELD_TAG)) {
				vals[k++] = childs.item(i).getTextContent();
			}
		}
		return vals;
	}

	/**
	 * В поля объекта obj заранее неизвестного класса заполняет данные из
	 * массива vals, список имен полей передается параметром fldsList
	 * Умеет работать с полями типа int и String
	 */
	private void setObjFlds(Object obj, String[] vals, String[] fldsList)
			throws NoSuchFieldException, SecurityException,
			NumberFormatException, IllegalArgumentException,
			IllegalAccessException {
		Class cls = obj.getClass();
		// минимум из размеров массива имен полей и массива значений
		int min = vals.length < fldsList.length ? vals.length : fldsList.length;
		for (int i = 0; i < min; i++) {
			String fld = fldsList[i];
			if (!fld.isEmpty()) {
				Field f = cls.getDeclaredField(fld);
				if (f.getType().toString().equals("int")) {
					f.setInt(obj, Integer.parseInt(vals[i]));
				} else {
					f.set(obj, vals[i]);
				}

			}
		}
	}

	/**
	 * Сообщает загрузчику имена классов в которые нужно преобразовывать объекты
	 * базы
	 * 
	 * @param classNames массив имен классов
	 */
	public void registerClassList(String[] classNames) throws ClassNotFoundException {
		for (int i = 0; i < classNames.length; i++) {
			Class<?> cls = Class.forName(classNames[i]);
			DBTable an = cls.getAnnotation(DBTable.class);
			if(an == null) continue;
			table2objectClassMap.put(an.TableName(), cls);
			table2columnOrder.put(an.TableName(), an.ColumnOrder().split(","));
		}
	}

}

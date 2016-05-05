package ru.enjoy.server;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ru.enjoy.server.exceptions.*;

/**
 * Загрузчик данных из файла базы данных в объекты
 * 
 * @author dm
 *
 */
public class DataBase2Object {
	private static final int MAX_FLDS_NUM = 11; // количество тегов внутри
												// записи
	private static final String DB_RECORD_TAG = "p"; // Тег обрамляющий запись
	private static final String DB_FIELD_TAG = "span"; // Тег обрамляющий поле
	
	// 

	// Карта соотвествия имени таблицы классу объекта
	private Map<String, Class<Object>> table2objectClassMap = new HashMap<>();
	// Имя таблицы - Порядок полей
	private Map<String, String[]> table2columnOrder = new HashMap<>();

	public void load(InputStream in, ArrayReceiver receiver)
			throws ParserConfigurationException, SAXException, IOException, BadDataAnnotationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(in);
		NodeList pList = doc.getElementsByTagName(DB_RECORD_TAG);
		for (int i = 0; i < pList.getLength(); i++) {
			receiver.putArray(getValsFromPTag(pList.item(i)));
		}
	}

	/**
	 * Получение массива данных из узла записи.
	 * (Разбор тегов DB_FIELD_TAG)
	 * 
	 * @param node
	 * @return
	 */
	private String[] getValsFromPTag(Node node) {
		String[] vals = new String[MAX_FLDS_NUM];
		NodeList childs = node.getChildNodes();
		for (int i = 0, k = 0; ((i < childs.getLength()) && (k < MAX_FLDS_NUM)); i++) {
			if (childs.item(i).getNodeName().equals(DB_FIELD_TAG)) {
				vals[k++] = childs.item(i).getTextContent();
			}
		}
		return vals;
	}
}

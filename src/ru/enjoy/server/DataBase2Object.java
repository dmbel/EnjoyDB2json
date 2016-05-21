package ru.enjoy.server;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ru.enjoy.server.exceptions.*;
import ru.enjoy.server.objbuilder.IArrayReceiver;

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

	public void load(InputStream in, IArrayReceiver receiver)
			throws ParserConfigurationException, SAXException, IOException,
			BadDataAnnotationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(in);
		NodeList pList = doc.getElementsByTagName(DB_RECORD_TAG);
		for (int i = 0; i < pList.getLength(); i++) {
			String[] vals = getValsFromPTag(pList.item(i));
			String type = vals[0];

			/**
			 * При загрузке для productSostavComponents есть ньюанс. Эта таблица
			 * может быть связана как с вариантом товара так и собственно с
			 * товаром. Есть 2 варианта связывания в зависимости пустое ли поле
			 * ProductSostavComponents.Variant Чтобы в дальнейшем не иметь
			 * проблем будем считать множества записей с пустой ссылкой на
			 * вариант и с непустой двумя разными сущностями. С этой целью
			 * вводим дополнительно виртуальную таблицу variantSostavComponents
			 * в нее включаем записи с непустым значением варианта
			 */

			if (type.equals("productSostavComponents")) {
				if (!vals[3].isEmpty()) {
					type = "variantSostavComponents";
				}
			}
			if (type.equals("productSpecialOffers")) {
				if (!vals[3].isEmpty()) {
					type = "variantSpecialOffers";
				}
			}
			if (type.equals("productImages")) {
				if (!vals[3].isEmpty()) {
					type = "variantImages";
				}
			}
			receiver.putArray(type, vals);
		}
	}

	/**
	 * Получение массива данных из узла записи. (Разбор тегов DB_FIELD_TAG)
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

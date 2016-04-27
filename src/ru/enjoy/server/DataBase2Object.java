package ru.enjoy.server;

import ru.enjoy.server.data.ObjectDataBase;
import ru.enjoy.server.data.Product;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Field;

/**
 * Загрузчик данных из файла базы данных
 * 
 * @author dm
 *
 */
public class DataBase2Object {

	// Здесь поля нужно перечислить в том порядке в котором они идут в span-ах
	private static final String[] FLDS_PRODUCT = { null, "id", "name", "type", "shortDesc", "comment", "url", "width",
			"height", "unitForPrice", "photoDesc" };

	public String fileName;

	public DataBase2Object(String fileName) {
		super();
		this.fileName = fileName;
	}

	public ObjectDataBase load() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException,
			NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		ObjectDataBase objectDataBase = new ObjectDataBase();
		Document doc = builder.parse(new FileInputStream(fileName));
		NodeList pList = doc.getElementsByTagName("p");
		for (int i = 0; i < pList.getLength(); i++) {
			String[] vals = getValsFromPTag(pList.item(i));
			switch (vals[0]) {
			case "products":
				Product p = new Product();
				//Product.class.getDeclaredField("id").setInt(p, Integer.parseInt(vals[1]));
				setObjFlds(p,vals,FLDS_PRODUCT);
				objectDataBase.products.add(p);
				objectDataBase.productIdIndex.put(p.id, p);
				break;
			}
		}
		return objectDataBase;
	}

	/**
	 * Вытащить массив из 11 строковых значений, лежащих во вложенных span
	 * 
	 * @param node
	 * @return
	 */
	private String[] getValsFromPTag(Node node) {
		String[] vals = new String[11];
		NodeList childs = node.getChildNodes();
		for (int i = 0, k = 0; i < childs.getLength(); i++) {
			if (childs.item(i).getNodeName().equals("span")) {
				vals[k++] = childs.item(i).getTextContent();
			}
			if (k == 11)
				break;
		}
		return vals;
	}

	private void setObjFlds(Object obj, String[] vals, String[] fldsList) throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Class cls = obj.getClass();
		for (int i = 1; i < vals.length; i++) {
			String fld = fldsList[i];
			if (fld != null) {
				Field f = cls.getDeclaredField(fld);
				if(f.getType().toString().equals("int")){
					f.setInt(obj, Integer.parseInt(vals[i]));
				} else {
					f.set(obj, vals[i]);
				}
				
			}
		}
	}

}

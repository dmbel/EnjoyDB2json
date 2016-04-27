package ru.enjoy.server;

import ru.enjoy.server.data.*;

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
	private static final String[] FLDS_CATEGORY = { null, "id", "type", "name", "url"};
	private static final String[] FLDS_PRODUCT_TYPE = { null, "id", "name", "mainCategoryId"};

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
			case "productCategories":
				Category c = new Category();
				setObjFlds(c,vals,FLDS_CATEGORY);
				objectDataBase.categories.add(c);
				objectDataBase.categoryIdIndex.put(c.id, c);
				break;
			case "productTypes":
				ProductType pt = new ProductType();
				setObjFlds(pt,vals,FLDS_PRODUCT_TYPE);
				objectDataBase.productTypes.add(pt);
				objectDataBase.productTypeIdIndex.put(pt.id, pt);
				break;
			}
		}
		return objectDataBase;
	}

	/**
	 * Вытащить массив из 11 строковых значений, лежащих во вложенном span
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

	/**
	 * В поля объекта obj заранее неизвестного класса заполняет данные из массива vals,
	 * список имен полей передается параметром fldsList
	 * @param obj
	 * @param vals
	 * @param fldsList
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setObjFlds(Object obj, String[] vals, String[] fldsList) throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Class cls = obj.getClass();
		for (int i = 0; (i < vals.length)&&(i < fldsList.length); i++) {
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

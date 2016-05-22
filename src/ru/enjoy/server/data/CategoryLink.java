package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productCategoriesLink", fieldsOrder = { "", "categoryId", "", "", "url" })
public class CategoryLink {
	public int categoryId;
	transient public String url;
}

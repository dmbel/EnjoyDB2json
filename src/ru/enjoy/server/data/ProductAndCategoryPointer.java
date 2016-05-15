package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productAndCategoryPointers", fieldsOrder = { "", "id", "productId", "categoryId", "variant",
		"rating" })
public class ProductAndCategoryPointer {
	transient public int id;
	public int productId;
	transient public int categoryId;
	public String variant;
	public String rating;
}

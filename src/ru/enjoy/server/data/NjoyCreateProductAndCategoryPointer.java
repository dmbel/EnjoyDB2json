package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "njoyCreateProductAndCategoryPointers", fieldsOrder = {
		"", "id", "categoryId", "productId", "rating" })
public class NjoyCreateProductAndCategoryPointer {
	transient public int id;
	transient public int categoryId;
	public int productId;
	public String rating;
}
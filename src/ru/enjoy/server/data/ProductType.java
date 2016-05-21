package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productTypes", fieldsOrder = { "", "id", "name",
		"mainCategoryId" })
public class ProductType {
	public int id;
	public String name;
	public int mainCategoryId;
}

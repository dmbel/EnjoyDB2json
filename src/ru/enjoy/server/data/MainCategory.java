package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "mainCategories", fieldsOrder = {"", "id", "categoryId"})
public class MainCategory {
	public int id;
	public int categoryId;
}

package ru.enjoy.server.data;

import java.util.List;

import ru.enjoy.server.data.specificator.Child;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productCategories", fieldsOrder = { "", "id", "type", "name", "" })
public class Category {
	public int id;
	public int type;
	public String name;
	@Child(childClass = "ru.enjoy.server.data.ProductAndCategoryPointer", idField = "id", refField = "categoryId")
	public List<ProductAndCategoryPointer> products;
}
package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.data.specificator.ChildList;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productCategories", fieldsOrder = { "", "id", "type", "name", "url" })
public class Category {
	public int id;
	public int type;
	public String name;
	public String url;
	@ChildList(childClass = "ru.enjoy.server.data.ProductAndCategoryPointer", idField = "id", refField = "categoryId")
	public List<ProductAndCategoryPointer> products = new ArrayList<>();
}
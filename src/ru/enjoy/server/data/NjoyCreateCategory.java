package ru.enjoy.server.data;

import java.util.List;

import ru.enjoy.server.data.specificator.Child;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "njoyCreateCategories", fieldsOrder = { "", "id",
		"name", "url" })
public class NjoyCreateCategory {
	public int id;
	public String name;
	public String url;
	@Child(childClass = "ru.enjoy.server.data.NjoyCreateProductAndCategoryPointer", idField = "id", refField = "categoryId")
	public List<NjoyCreateProductAndCategoryPointer> products;
}

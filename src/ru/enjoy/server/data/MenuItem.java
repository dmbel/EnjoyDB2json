package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.Child;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "menuPunkts", fieldsOrder = { "", "id", "level", "name", "url", "levelFirst", "position" })
public class MenuItem {
	public int id;
	public int level;
	public String name;
	transient public String url;
	public int levelFirst;
	public int position;
	@Child(childClass = "ru.enjoy.server.data.CategoryLink", idField = "url", refField = "url")
	transient public CategoryLink category;
	public int categoryId;
}

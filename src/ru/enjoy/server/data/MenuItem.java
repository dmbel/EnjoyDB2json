package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "menuPunkts", fieldsOrder = { "", "id", "level", "name",
		"url", "levelFirst", "position" })
public class MenuItem {
	public int id;
	public int level;
	public String name;
	public String url;
	public int levelFirst;
	public int position;
}

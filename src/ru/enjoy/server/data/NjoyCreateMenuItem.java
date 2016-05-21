package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "njoyCreateMenuPunkts", fieldsOrder = { "", "id",
		"name", "url", "position", "display" })
public class NjoyCreateMenuItem {
	public int id;
	public String name;
	public String url;
	public String position;
	public String display;
}

package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productParameters", fieldsOrder = {"", "id", "name", "unit"})
public class ProductParameter {
	public int id;
	public String name;
	public String unit;
}

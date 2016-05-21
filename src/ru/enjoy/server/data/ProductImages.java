package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productImages", fieldsOrder = { "", "id", "productId",
		"", "url", "main" })
public class ProductImages {
	transient public long id;
	transient public int productId;
	public String url;
	public int main;
}
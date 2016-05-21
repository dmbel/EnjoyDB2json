package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productImages", fieldsOrder = { "", "id", "productId",
		"variant", "url", "main" })
public class VariantImages {
	transient public long id;
	transient public int productId;
	transient public String variant;
	public String url;
	public int main;
}
package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productVariants", fieldsOrder = { "", "id", "productId", "variant", "parameterValue", "main" })
public class ProductVariant {
	transient public long id;
	transient public int productId;
	public String variant;
	public String parameterValue;
	public String main;
}

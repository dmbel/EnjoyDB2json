package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productSostavComponents", fieldsOrder = { "", "id", "productId", "", "componentId", "amount", "variation"})
public class ProductSostavComponent {
	transient public int id;
	transient public int productId;
	public String componentId;
	public String amount;
	public String variation;
}
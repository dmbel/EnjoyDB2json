package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "variantSostavComponents", fieldsOrder = { "", "id", "productId", "variant", "componentId", "componentAmount", "variation"})
public class VariantSostavComponent {
	transient public int id;
	transient public int productId;
	transient public String variant;
	public String componentId;
	public String componentAmount;
	public String variation;
}
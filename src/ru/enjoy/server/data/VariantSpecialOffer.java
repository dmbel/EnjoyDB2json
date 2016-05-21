package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "variantSpecialOffers", fieldsOrder = { "", "id",
		"productId", "variant", "variation", "price", "componentPrice" })
public class VariantSpecialOffer {
	public int id;
	public int productId;
	public String variant;
	public String variation;
	public String price;
	public String componentPrice;
}

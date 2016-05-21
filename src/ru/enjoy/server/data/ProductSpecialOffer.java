package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productSpecialOffers", fieldsOrder = { "", "id",
		"productId", "", "variation", "price", "componentPrice" })
public class ProductSpecialOffer {
	public int id;
	public int productId;
	public String variation;
	public String price;
	public String componentPrice;
}
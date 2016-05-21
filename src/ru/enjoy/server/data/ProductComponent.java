package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productComponents", fieldsOrder = { "", "id", "quant", "price", "comment" })
public class ProductComponent {
	public int id;
	public String name;
	public String quant;
	public String price;
	public String comment;
}

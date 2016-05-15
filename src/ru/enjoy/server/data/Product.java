package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;
import ru.enjoy.server.data.specificator.ChildList;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "products", fieldsOrder = { "", "id", "name", "type", "shortDesc", "comment", "url", "width",
		"height", "unitForPrice", "photoDesc" })
public class Product {
	public int id;
	public String name;
	transient public int type;
	public String shortDesc;
	public String comment;
	public String url;
	public String width;
	public String height;
	public String unitForPrice;
	public String photoDesc;
	@ChildList(childClass = "ru.enjoy.server.data.ProductVariant", idField = "id", refField = "productId")
	public List<ProductVariant> variants = new ArrayList<>();
}

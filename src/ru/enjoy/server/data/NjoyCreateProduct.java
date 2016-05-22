package ru.enjoy.server.data;

import java.util.List;

import ru.enjoy.server.data.specificator.Child;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "njoyCreateProducts", fieldsOrder = {"", "id", "price", "shortDesc"})
public class NjoyCreateProduct {
	public int id;
	public String price;
	public String shortDesc;
	@Child(childClass="ru.enjoy.server.data.NjoyCreateProductImage", idField="id", refField="productId")
	public List<NjoyCreateProductImage> images;
}

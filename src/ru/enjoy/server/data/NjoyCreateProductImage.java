package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "njoyCreateProductImages", fieldsOrder = {"","id","productId","url"})
public class NjoyCreateProductImage {
	transient public int id;
	transient public int productId;
	public String url;
}
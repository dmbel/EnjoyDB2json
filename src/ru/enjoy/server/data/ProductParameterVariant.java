package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode="productParameterVariants", fieldsOrder={"","id","productId","parameterId"})
public class ProductParameterVariant {
	transient public int id;
	transient public int productId;
	public int parameterId;
}
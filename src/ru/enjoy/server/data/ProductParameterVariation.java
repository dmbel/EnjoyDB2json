package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode="productParameterVariations", fieldsOrder={"","id","productId","parameterId"})
public class ProductParameterVariation {
	transient public int id;
	transient public int productId;
	public int parameterId;
}

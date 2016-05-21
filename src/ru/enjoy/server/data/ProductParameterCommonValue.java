package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productParameterCommonValues", fieldsOrder = { "",
		"id", "productId", "productParameterId", "parameterValue" })
public class ProductParameterCommonValue {
	transient public int id;
	transient public int productId;
	public int productParameterId;
	public String parameterValue;
}
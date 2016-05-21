package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "paymentTypes", fieldsOrder = { "", "id", "name",
		"systemId" })
public class PaymentType {
	public int id;
	public String name;
	public int systemId;
}
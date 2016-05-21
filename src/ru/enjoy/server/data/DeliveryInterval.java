package ru.enjoy.server.data;

import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "deliveryIntervals", fieldsOrder = { "", "id",
		"timeBegin", "timeEnd", "tnterval", "tarif1", "tarif2", "descript",
		"rating" })
public class DeliveryInterval {
	public int id;
	public String timeBegin;
	public String timeEnd;
	public String tnterval;
	public String tarif1;
	public String tarif2;
	public String descript;
	public String rating;
}
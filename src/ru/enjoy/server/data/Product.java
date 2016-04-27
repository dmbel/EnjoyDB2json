package ru.enjoy.server.data;

import ru.enjoy.server.DBTable;

@DBTable(TableName = "products", ColumnOrder = ",id,name,type,shortDesc,comment,url,width,height,unitForPrice,photoDesc")
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
}

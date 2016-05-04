package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.DBTable;
@DBTable(TableName = "products", JsonListName="products", ColumnOrder = ",id,name,type,shortDesc,comment,url,width,height,unitForPrice,photoDesc")
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
	public List<ProductVariant> variants = new ArrayList<>();
}

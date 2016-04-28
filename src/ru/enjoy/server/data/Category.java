package ru.enjoy.server.data;

import ru.enjoy.server.DBTable;

@DBTable(TableName = "productCategories", JsonListName="categories", ColumnOrder = ",id,type,name,url")
public class Category {
	public int id;
	public int type;
	public String name;
	public String url;
}
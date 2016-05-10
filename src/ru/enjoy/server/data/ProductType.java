package ru.enjoy.server.data;

import ru.enjoy.server.json.DBTable;

@DBTable(TableName = "productTypes", JsonListName="productTypes", ColumnOrder = ",id,name,mainCategoryId")
public class ProductType {
	public int id;
	public String name;
	public int mainCategoryId;
}

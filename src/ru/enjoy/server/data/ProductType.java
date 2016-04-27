package ru.enjoy.server.data;

import ru.enjoy.server.DBTable;

@DBTable(TableName = "productTypes", ColumnOrder = ",id,name,mainCategoryId")
public class ProductType {
	public int id;
	public String name;
	public int mainCategoryId;
}

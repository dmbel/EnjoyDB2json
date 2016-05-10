package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.json.DBTable;
import ru.enjoy.server.json.ChildList;

@DBTable(TableName = "productCategories", JsonListName = "categories", ColumnOrder = ",id,type,name,url")
public class Category {
	public int id;
	public int type;
	public String name;
	public String url;
	@ChildList(key = "id", ref = "categoryId")
	public List<ProductAndCategoryPointer> products = new ArrayList<>();
}
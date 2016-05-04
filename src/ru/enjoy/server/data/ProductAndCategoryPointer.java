package ru.enjoy.server.data;

import ru.enjoy.server.DBTable;
import ru.enjoy.server.ChildEntity;

@DBTable(TableName = "productAndCategoryPointers", JsonListName = "productAndCategoryPointers", ColumnOrder = ",id,productId,categoryId,variant,rating")
@ChildEntity(parentClass = "ru.enjoy.server.data.Category", parentIdField = "id", parentListField = "products", referenceField = "categoryId")
public class ProductAndCategoryPointer {
	transient public int id;
	public int productId;
	transient public int categoryId;
	public String variant;
	public String rating;
}

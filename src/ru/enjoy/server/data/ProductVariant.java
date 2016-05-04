package ru.enjoy.server.data;

import ru.enjoy.server.DBTable;
import ru.enjoy.server.ChildEntity;

@DBTable(TableName = "productVariants", JsonListName = "productVariants", ColumnOrder = ",id,productId,variant,parameterValue,main")
@ChildEntity(parentClass = "ru.enjoy.server.data.Product", parentIdField = "id", parentListField = "variants", referenceField = "productId")
public class ProductVariant {
	transient public int id;
	transient public int productId;
	public String variant;
	public String parameterValue;
	public String main;
}

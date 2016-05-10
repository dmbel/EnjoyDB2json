package ru.enjoy.server.json;

import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface DBTable {
	String TableName();
	String JsonListName();
	String ColumnOrder();
}

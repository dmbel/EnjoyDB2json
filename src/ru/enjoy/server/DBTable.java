package ru.enjoy.server;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
public @interface DBTable {
	String TableName();
	String ColumnOrder();
}

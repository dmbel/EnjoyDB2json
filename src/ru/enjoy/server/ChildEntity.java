package ru.enjoy.server;

import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ChildEntity {
	String parentClass();
	String parentListField();
	String referenceField();
	String parentIdField();
}

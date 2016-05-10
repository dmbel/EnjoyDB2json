package ru.enjoy.server.json;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ChildList {
	String key();
	String ref();
}

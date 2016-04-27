package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectDataBase {
	public List<Product> products;
	public List<Category> categories;
	public transient Map<Integer,Product> productIdIndex;
	public transient Map<Integer,Product> categoryIdIndex;
	
	public ObjectDataBase(){
		products = new ArrayList<>();
		categories = new ArrayList<>();
		productIdIndex = new HashMap<>();
		categoryIdIndex = new HashMap<>();
		
	}
}

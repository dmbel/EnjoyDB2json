package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectDataBase {
	public List<Product> products = new ArrayList<>();
	public List<Category> categories = new ArrayList<>();
	public List<ProductType> productTypes = new ArrayList<>();
	public transient Map<Integer,Product> productIdIndex = new HashMap<>();
	public transient Map<Integer,Category> categoryIdIndex = new HashMap<>();
	public transient Map<Integer,ProductType> productTypeIdIndex = new HashMap<>();
	
}

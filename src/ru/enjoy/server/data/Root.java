package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

public class Root {
	public List<Product> products = new ArrayList<>();
	public List<Category> categories = new ArrayList<>();
	public List<ProductType> productTypes = new ArrayList<>();
	transient public List<ProductVariant> productVariants = new ArrayList<>();
	transient public List<ProductAndCategoryPointer> productAndCategoryPointers = new ArrayList<>();
}

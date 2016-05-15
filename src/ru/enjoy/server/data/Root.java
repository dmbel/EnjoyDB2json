package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.data.specificator.ChildList;

public class Root {
	@ChildList(childClass = "ru.enjoy.server.data.Product", idField = "", refField = "")
	public List<Product> products = new ArrayList<>();
	@ChildList(childClass = "ru.enjoy.server.data.Category", idField = "", refField = "")
	public List<Category> categories = new ArrayList<>();
	//public List<ProductType> productTypes = new ArrayList<>();
}

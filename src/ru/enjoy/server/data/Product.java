package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.data.specificator.Child;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "products", fieldsOrder = { "", "id", "name", "type",
		"shortDesc", "comment", "url", "width", "height", "unitForPrice",
		"photoDesc" })
public class Product {
	public int id;
	public String name;
	transient public int type;
	public String shortDesc;
	public String comment;
	public String url;
	public String width;
	public String height;
	public String unitForPrice;
	public String photoDesc;

	@Child(childClass = "ru.enjoy.server.data.ProductSostavComponent", idField = "id", refField = "productId")
	public List<ProductSostavComponent> sostav;

	@Child(childClass = "ru.enjoy.server.data.Variant", idField = "id", refField = "productId")
	public List<Variant> variants = new ArrayList<>();

	@Child(childClass = "ru.enjoy.server.data.ProductSpecialOffer", idField = "id", refField = "productId")
	public List<ProductSpecialOffer> specialOffers = new ArrayList<>();

	@Child(childClass = "ru.enjoy.server.data.ProductImages", idField = "id", refField = "productId")
	public List<ProductImages> images = new ArrayList<>();

	@Child(childClass = "ru.enjoy.server.data.ProductParameterCommonValue", idField = "id", refField = "productId")
	public List<ProductParameterCommonValue> paramCommonValues = new ArrayList<>();
	
	@Child(childClass = "ru.enjoy.server.data.ProductParameterVariant", idField = "id", refField = "productId")
	public List<ProductParameterVariant> parameterVariants = new ArrayList<>();

	@Child(childClass = "ru.enjoy.server.data.ProductParameterVariation", idField = "id", refField = "productId")
	public List<ProductParameterVariation> parameterVariations = new ArrayList<>();
	
}

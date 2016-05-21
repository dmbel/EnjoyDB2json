package ru.enjoy.server.data;

import java.util.ArrayList;
import java.util.List;

import ru.enjoy.server.data.specificator.ChildList;
import ru.enjoy.server.data.specificator.DataObject;

@DataObject(typeCode = "productVariants", fieldsOrder = { "", "id",
		"productId", "variant", "parameterValue", "main" })
public class Variant {
	transient public int id;
	transient public int productId;
	public String variant;
	public String parameterValue;
	public String main;
	@ChildList(childClass = "ru.enjoy.server.data.VariantSostavComponent", idField = {
			"variant", "productId" }, refField = { "variant", "productId" })
	public List<VariantSostavComponent> sostav = new ArrayList<>();
	@ChildList(childClass = "ru.enjoy.server.data.VariantSpecialOffer", idField = {
			"productId", "variant" }, refField = { "productId", "variant" })
	public List<VariantSpecialOffer> specialOffers = new ArrayList<>();
	@ChildList(childClass = "ru.enjoy.server.data.VariantImages", idField = {"productId","variant"}, refField = {"productId","variant"})
	public List<VariantImages> images = new ArrayList<>();

}
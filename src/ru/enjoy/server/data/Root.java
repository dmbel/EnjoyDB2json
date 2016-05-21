package ru.enjoy.server.data;

import java.util.List;

import ru.enjoy.server.data.specificator.DataObject;
import ru.enjoy.server.data.specificator.TableList;
@DataObject(typeCode="ROOT",fieldsOrder={})
public class Root {
	@TableList(cls = "ru.enjoy.server.data.Product")
	public List<Product> products;
	@TableList(cls = "ru.enjoy.server.data.Category")
	public List<Category> categories;
	@TableList(cls = "ru.enjoy.server.data.ProductParameter")
	public List<ProductParameter> productParams; 
	@TableList(cls = "ru.enjoy.server.data.ProductType")
	public List<ProductType> productTypes; 
	@TableList(cls = "ru.enjoy.server.data.MenuItem")
	public List<MenuItem> menuItems; 
	@TableList(cls = "ru.enjoy.server.data.DeliveryInterval")
	public List<DeliveryInterval> deliveryIntervals; 
	@TableList(cls = "ru.enjoy.server.data.PaymentType")
	public List<PaymentType> paymentTypes; 
	@TableList(cls = "ru.enjoy.server.data.MainCategory")
	public List<MainCategory> mainCategories; 
	@TableList(cls = "ru.enjoy.server.data.NjoyCreateProduct")
	public List<NjoyCreateProduct> njoyCreateProducts; 
	@TableList(cls = "ru.enjoy.server.data.NjoyCreateCategory")
	public List<NjoyCreateCategory> njoyCreateCategories; 
	@TableList(cls = "ru.enjoy.server.data.NjoyCreateMenuItem")
	public List<NjoyCreateMenuItem> njoyCreateMenuItems; 
	
}

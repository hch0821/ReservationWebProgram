package com.chung.service;

import java.util.List;

import com.chung.dto.Category;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;

public interface IService {
	public interface Main {
		public static final int NUM_ITEM = 4;

		public List<Product> getProducts(Integer categoryId, Integer start);

		public List<Category> getCategories();

		public List<Promotion> getPromotions();

		public List<ProductImage> getProductImage(Integer productId, String type);
	}

}

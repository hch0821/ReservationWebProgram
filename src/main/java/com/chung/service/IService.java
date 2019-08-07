package com.chung.service;

import java.util.List;

import com.chung.dto.Category;
import com.chung.dto.Comment;
import com.chung.dto.DisplayInfo;
import com.chung.dto.DisplayInfoImage;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.ProductPrice;
import com.chung.dto.Promotion;

public interface IService {
	public interface Main {
		public static final int NUM_ITEM = 4;

		public List<Product> getProducts(Integer categoryId, Integer start);

		public List<Category> getCategories();

		public List<Promotion> getPromotions();

		public ProductImage getProductImage(Integer productId, String type);
	}

	public interface Detail {
		public Double getAverageScore(List<Comment> comments);

		public List<Comment> getComments(Integer displayInfoId);

		public DisplayInfo getDisplayInfo(Integer displayInfoId);

		public DisplayInfoImage getDisplayInfoImage(Integer displayInfoId);

		public List<ProductImage> getProductImages(Integer productId);

		public List<ProductPrice> getProductPrices(Integer productId);
	}

}

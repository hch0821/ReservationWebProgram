package com.chung.service;

import java.util.List;

import com.chung.dto.category.Category;
import com.chung.dto.product.ProductImage;
import com.chung.dto.promotion.Promotion;

public interface IPromotionAndCategoryService {
	public List<Promotion> getPromotions();

	public List<Category> getCategories();
}

package com.chung.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.CategoryAndPromotionDao;
import com.chung.dao.ProductDao;
import com.chung.dto.category.Category;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.dto.promotion.Promotion;
import com.chung.service.IProductService;
import com.chung.service.IPromotionAndCategoryService;

@Service
public class MainService implements IProductService, IPromotionAndCategoryService {

	@Autowired
	ProductDao productDao;

	@Autowired
	CategoryAndPromotionDao categoryAndPromotionDao;

	@Override
	public List<Product> getProducts(int categoryId, int start) {
		List<Product> list = null;
		if (categoryId == 0) {
			list = productDao.selectAllProduct(start);
		} else {
			list = productDao.selectProducts(categoryId, start);
		}
		for (Product product : list) {
			product.setProductImageUrl(
					"/api/productImages/" + product.getProductId() + "?type=" + ProductImage.Type.TYPE_TH);
		}
		return list;

	}

	@Override
	public List<Category> getCategories() {
		List<Category> categories = categoryAndPromotionDao.selectCategory();
		List<Integer> productCount = productDao.selectProductCount();
		int allProductCntSum = 0;
		int i, cnt;
		Category allCategory = new Category();
		allCategory.setName("전체 리스트");
		allCategory.setId(0);

		for (i = 0; i < categories.size(); i++) {
			cnt = productCount.get(i);
			allProductCntSum += cnt;
			categories.get(i).setCount(cnt);
		}
		allCategory.setCount(allProductCntSum);

		categories.add(0, allCategory);
		return categories;
	}

	@Override
	public List<Promotion> getPromotions() {
		List<Promotion> promotions = categoryAndPromotionDao.selectPromotion();
		for (Promotion pro : promotions) {
			pro.setProductImageUrl("/api/productImages/" + pro.getProductId() + "?type=" + ProductImage.Type.TYPE_TH);
		}

		return promotions;
	}

	@Override
	public ProductImage getProductImage(int productId, String type) {
		return productDao.selectProductImage(productId, type);
	}

	@Override
	public List<ProductPrice> getProductPrices(int productId) {
		return new ArrayList<ProductPrice>();
	}

	@Override
	public List<ProductImage> getProductImages(int productId) {
		return new ArrayList<ProductImage>();
	}
}

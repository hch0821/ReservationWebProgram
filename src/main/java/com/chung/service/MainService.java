package com.chung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.ReservDao;
import com.chung.dto.category.Category;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.dto.promotion.Promotion;

@Service
public class MainService implements IProductService {

	@Autowired
	ReservDao reservDao;

	@Override
	public List<Product> getProducts(int categoryId, int start) {
		List<Product> list = null;
		if (categoryId == 0) {
			list = reservDao.selectAllProduct(start);
		} else {
			list = reservDao.selectProducts(categoryId, start);
		}
		for (Product prod : list) {
			prod.setProductImageUrl(
					"/reserv/api/productImages/" + prod.getProductId() + "?type=" + ProductImage.Type.TYPE_TH);
		}
		return list;

	}

	public List<Category> getCategories() {
		List<Category> categories = reservDao.selectCategory();
		List<Integer> productCount = reservDao.selectProductCount();
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

	public List<Promotion> getPromotions() {
		List<Promotion> promotions = reservDao.selectPromotion();
		for (Promotion pro : promotions) {
			pro.setProductImageUrl(
					"/reserv/api/productImages/" + pro.getProductId() + "?type=" + ProductImage.Type.TYPE_TH);
		}

		return promotions;
	}

	@Override
	public ProductImage getProductImage(int productId, String type) {
		return reservDao.selectProductImage(productId, type);
	}

	@Override
	public List<ProductPrice> getProductPrices(int productId) {
		return null;
	}

	@Override
	public List<ProductImage> getProductImages(int productId) {
		return null;
	}
}

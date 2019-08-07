package com.chung.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.MainDao;
import com.chung.dto.Category;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;

@Service
public class MainService implements IService.Main {

	@Autowired
	MainDao mainDao;

	@Override
	public List<Product> getProducts(Integer categoryId, Integer start) {
		List<Product> list = null;
		if (categoryId.intValue() == 0) {
			list = mainDao.selectAllProduct(start);
		} else {
			list = mainDao.selectProducts(categoryId, start);
		}
		for (Product prod : list) {
			prod.setProductImageUrl("http://localhost:8080/productImages/" + prod.getProductId() + "?type="
					+ ProductImage.Type.TYPE_TH);
		}
		return list;

	}

	@Override
	public List<Category> getCategories() {
		List<Category> categories = mainDao.selectCategory();
		List<Integer> productCount = mainDao.selectProductCount();
		int allProductCntSum = 0;
		int i, cnt;
		Category allCate = new Category();
		allCate.setName("전체 리스트");
		allCate.setId(0);

		for (i = 0; i < categories.size(); i++) {
			cnt = productCount.get(i);
			allProductCntSum += cnt;
			categories.get(i).setCount(cnt);
		}
		allCate.setCount(allProductCntSum);

		categories.add(0, allCate);
		return categories;
	}

	@Override
	public List<Promotion> getPromotions() {
		List<Promotion> promotions = mainDao.selectPromotion();
		for (Promotion pro : promotions) {
			pro.setProductImageUrl("http://localhost:8080/reserv/api/productImages/" + pro.getProductId() + "?type="
					+ ProductImage.Type.TYPE_TH);
		}

		return promotions;
	}

	@Override
	public ProductImage getProductImage(Integer productId, String type) {
		return mainDao.selectProductImage(productId, type);
	}
}

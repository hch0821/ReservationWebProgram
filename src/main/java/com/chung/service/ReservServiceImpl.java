package com.chung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chung.dao.ReservDao;
import com.chung.dto.Category;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;

@Service
public class ReservServiceImpl implements ReservService {

	@Autowired
	ReservDao reservDao;
	
	@Override
	@Transactional //readonly
	public List<Product> getProducts(Integer categoryId, Integer start) {
		List<Product> list = null;
		if(categoryId.intValue() == 0)
		{
			list = reservDao.selectAllProduct(start);
		}
		else {
			list = reservDao.selectProduct(categoryId, start);
		}
		
		for(Product prod : list)
		{
			prod.setProductImageUrl("http://localhost:8080/productImages/"+prod.getProductId()+"?type=th");
		}
		return list;
			
	}

	@Override
	@Transactional //readonly
	public List<Category> getCategories() {
		List<Category> categories = reservDao.selectCategory();
		for(Category c : categories)
		{
			int count = reservDao.selectProductCount(c.getId());
			c.setCount(count);
		}
		return categories;
	}

	@Override
	@Transactional //readonly
	public List<Promotion> getPromotions() {
		// TODO Auto-generated method stub
		List<Promotion> promotions = reservDao.selectPromotion();
		for(Promotion pro : promotions)
		{
			pro.setProductImageUrl("http://localhost:8080/reserv/api/productImages/"+pro.getProductId()+"?type=th");
		}
		
		return promotions;
	}

	@Override
	public int getProductCount(Integer categoryId) {
		// TODO Auto-generated method stub
		if(categoryId.intValue() == 0)
			return reservDao.selectAllProductCount();
		else
			return reservDao.selectProductCount(categoryId);
	}

	@Override
	public List<ProductImage> getProductImage(Integer productId, String type) {
		// TODO Auto-generated method stub
		return reservDao.selectProductImage(productId, type);
	}



}

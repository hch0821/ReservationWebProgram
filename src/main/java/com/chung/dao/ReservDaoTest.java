package com.chung.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.chung.config.ApplicationConfig;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;

public class ReservDaoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		ReservDao reservDao = ac.getBean(ReservDao.class);
		
//		System.out.println(reservDao.selectCategoryCount(2));
//		System.out.println(reservDao.selectAllCategoryCount());
		
		List<Promotion> promotions = reservDao.selectPromotion();
		for(Promotion pro : promotions)
		{
			pro.setProductImageUrl("http://localhost:8080/reserv/api/productImages/"+pro.getProductId()+"?type=th");
		}
		
		System.out.println(promotions);
		
//		List<Category> categories = reservDao.selectCategory();
//		for(Category c : categories)
//		{
//			int count = reservDao.selectCategoryCount(c.getId());
//			c.setCount(count);
//		}
//		System.out.println(categories);
//		
//		List<Product> list = reservDao.selectAllProduct(0);
//		for(Product prod : list)
//		{
//			prod.setProductImageUrl("http://localhost:8080/productImages/"+prod.getProductId()+"?type=th");
//		}
//		System.out.println(list);
		
//		System.out.println(reservDao.selectProductImage(2, "th").get(0));
	}

}

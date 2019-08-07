package com.chung.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.chung.config.ApplicationConfig;
import com.chung.dto.Comment;
import com.chung.dto.DisplayInfo;
import com.chung.dto.DisplayInfoImage;
import com.chung.dto.ProductImage;
import com.chung.dto.ProductPrice;


public class DaoTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		DetailDao detailDao = ac.getBean(DetailDao.class);
		
		DisplayInfo displayInfo = detailDao.selectDisplayInfo(1);
		
		System.out.println(displayInfo);
	
		List<ProductImage> productImages = detailDao.selectProductImages(displayInfo.getProductId());
		System.out.println(productImages);
		
		List<ProductPrice> prices = detailDao.selectProducPrices(displayInfo.getProductId());
		System.out.println(prices);
		
		DisplayInfoImage displayInfoImage = detailDao.selectDisplayInfoImage(1);
		
		System.out.println(displayInfoImage);
		
		List<Comment> comments = detailDao.selectComments(1);
		
		double avgScore = 0.0;
		for(Comment c : comments) {
			c.setCommentImages(detailDao.selectCommentImages(c.getCommentId()));
			avgScore += c.getScore();
			System.out.println(c);
		}
		avgScore /= comments.size();
		System.out.println(avgScore);
	}

}

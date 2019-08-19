package com.chung.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.CommentDao;
import com.chung.dao.DisplayInfoDao;
import com.chung.dao.ProductDao;
import com.chung.dto.comment.Comment;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.service.IDisplayService;
import com.chung.service.IProductService;
import com.chung.service.IRateService;

@Service
public class DetailService implements IRateService, IDisplayService, IProductService {

	@Autowired
	CommentDao commentDao;

	@Autowired
	DisplayInfoDao displayInfoDao;

	@Autowired
	ProductDao productDao;

	@Override
	public Double getAverageScore(List<Comment> comments) {
		double avgScore = 0.0;
		if (comments.isEmpty())
			return avgScore;

		for (Comment c : comments) {
			avgScore += c.getScore();
		}
		avgScore /= comments.size();
		return avgScore;
	}

	@Override
	public List<Comment> getComments(int displayInfoId) {
		List<Comment> comments = commentDao.selectComments(displayInfoId);
		for (Comment c : comments) {
			c.setCommentImages(commentDao.selectCommentImages(c.getCommentId()));
		}
		return comments;
	}

	@Override
	public DisplayInfo getDisplayInfo(int displayInfoId) {

		return displayInfoDao.selectDisplayInfo(displayInfoId);
	}

	@Override
	public DisplayInfoImage getDisplayInfoImage(int displayInfoId) {
		return displayInfoDao.selectDisplayInfoImage(displayInfoId);
	}

	@Override
	public List<ProductImage> getProductImages(int productId) {
		return productDao.selectProductImages(productId);
	}

	@Override
	public List<ProductPrice> getProductPrices(int productId) {
		return productDao.selectProductPrices(productId);
	}

	@Override
	public List<Product> getProducts(int categoryId, int start) {
		return new ArrayList<>();
	}

	@Override
	public ProductImage getProductImage(int productId, String type) {
		return new ProductImage();
	}

}
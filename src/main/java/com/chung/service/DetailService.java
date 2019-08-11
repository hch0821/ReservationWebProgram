package com.chung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.ReservDao;
import com.chung.dto.comment.Comment;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;

@Service
public class DetailService implements IRateService, IDisplayService, IProductService {

	@Autowired
	ReservDao reservDao;

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
		List<Comment> comments = reservDao.selectComments(displayInfoId);
		for (Comment c : comments) {
			c.setCommentImages(reservDao.selectCommentImages(c.getCommentId()));
		}
		return comments;
	}

	@Override
	public DisplayInfo getDisplayInfo(int displayInfoId) {

		return reservDao.selectDisplayInfo(displayInfoId);
	}

	@Override
	public DisplayInfoImage getDisplayInfoImage(int displayInfoId) {
		return reservDao.selectDisplayInfoImage(displayInfoId);
	}

	@Override
	public List<ProductImage> getProductImages(int productId) {
		return reservDao.selectProductImages(productId);
	}

	@Override
	public List<ProductPrice> getProductPrices(int productId) {
		return reservDao.selectProducPrices(productId);
	}

	@Override
	public List<Product> getProducts(int categoryId, int start) {
		return null;
	}

	@Override
	public ProductImage getProductImage(int productId, String type) {
		return null;
	}

}
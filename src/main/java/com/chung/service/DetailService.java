package com.chung.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.DetailDao;
import com.chung.dto.Comment;
import com.chung.dto.DisplayInfo;
import com.chung.dto.DisplayInfoImage;
import com.chung.dto.ProductImage;
import com.chung.dto.ProductPrice;


@Service
public class DetailService implements IService.Detail{

	@Autowired
	DetailDao detailDao;

	@Override
	public Double getAverageScore(List<Comment> comments) {
		double avgScore = 0.0;
		if(comments.size() == 0) 
			return avgScore;
		
		for(Comment c : comments) {
			avgScore += c.getScore();
		}
		avgScore /= comments.size();
		return avgScore;
	}

	@Override
	public List<Comment> getComments(Integer displayInfoId) {
		List<Comment> comments = detailDao.selectComments(displayInfoId);
		for(Comment c : comments) {
			c.setCommentImages(detailDao.selectCommentImages(c.getCommentId()));
		}
		return comments;
	}

	@Override
	public DisplayInfo getDisplayInfo(Integer displayInfoId) {
		return detailDao.selectDisplayInfo(displayInfoId);
	}

	@Override
	public DisplayInfoImage getDisplayInfoImage(Integer displayInfoId) {
		return detailDao.selectDisplayInfoImage(displayInfoId);
	}

	@Override
	public List<ProductImage> getProductImages(Integer productId) {
		return detailDao.selectProductImages(productId);
	}

	@Override
	public List<ProductPrice> getProductPrices(Integer productId) {
		return detailDao.selectProducPrices(productId);
	}
	
	

}

package com.chung.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chung.dto.comment.Comment;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.service.impl.DetailService;

//=======================================================================
//상세 화면을 위한 API 컨트롤
//=======================================================================

@RestController
@RequestMapping(path = "/api")
public class DetailPageApiController {

	@Autowired
	DetailService detailService;

	// http://localhost:8080/reserv/api/products/1
	@GetMapping("/products/{displayInfoId}")
	public Map<String, Object> displayInfoResponse(@PathVariable(name = "displayInfoId") Integer displayInfoId) {
		Map<String, Object> map = new HashMap<>();

		List<Comment> comments = detailService.getComments(displayInfoId);
		Double averageScore = Double.parseDouble(String.format("%.1f", detailService.getAverageScore(comments)));
		DisplayInfo displayInfo = detailService.getDisplayInfo(displayInfoId);
		DisplayInfoImage displayInfoImage = detailService.getDisplayInfoImage(displayInfoId);
		List<ProductImage> productImages = detailService.getProductImages(displayInfo.getProductId());
		List<ProductPrice> productPrices = detailService.getProductPrices(displayInfo.getProductId());

		map.put("averageScore", averageScore);
		map.put("comments", comments);
		map.put("displayInfo", displayInfo);
		map.put("displayInfoImage", displayInfoImage);
		map.put("productImages", productImages);
		map.put("productPrices", productPrices);
		return map;
	}
}

package com.chung.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.chung.dto.Category;
import com.chung.dto.Comment;
import com.chung.dto.DisplayInfo;
import com.chung.dto.DisplayInfoImage;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.ProductPrice;
import com.chung.dto.Promotion;
import com.chung.service.IService;

@RestController
@RequestMapping(path = "/api")
public class ReservController {
	@Autowired
	IService.Main mainService;
	
	@Autowired
	IService.Detail detailService;
	
//=======================================================================
//메인 화면을 위한 컨트롤
//=======================================================================	
	// http://localhost:8080/reserv/api/products?categoryId=3&start=1
	@GetMapping("/products")
	public Map<String, Object> productResponse(@RequestParam(name = "categoryId", required = true) int categoryId,
			@RequestParam(name = "start", required = false, defaultValue = "0") int start) {
		List<Product> products = mainService.getProducts(categoryId, start);
		Map<String, Object> map = new HashMap<>();
		map.put("items", products);
		map.put("totalCount", mainService.getCategories().get(categoryId).getCount());
		return map;
	}

	// http://localhost:8080/reserv/api/categories
	@GetMapping("/categories")
	public Map<String, Object> categoryResponse() {
		List<Category> categories = mainService.getCategories();
		Map<String, Object> map = new HashMap<>();
		map.put("items", categories);
		return map;
	}

	// http://localhost:8080/reserv/api/promotions
	@GetMapping("/promotions")
	public Map<String, Object> promotionResponse() {
		List<Promotion> promotions = mainService.getPromotions();
		Map<String, Object> map = new HashMap<>();
		map.put("items", promotions);
		return map;
	}

	// http://localhost:8080/reserv/api/productImages/{productId}?type=th"
	@GetMapping("/productImages/{productId}")
	public RedirectView getProductImageByProductId(@PathVariable(name = "productId") Integer productId,
			@RequestParam(name = "type", required = true) String type) {
		ProductImage productImage = mainService.getProductImage(productId, type);
		return new RedirectView("/reserv/res/" + productImage.getSaveFileName());
	}

	// http://localhost:8080/reserv/api/productImages/{productId}/{productImageId}
	@GetMapping("/productImages/{productId}/{productImageId}")
	public RedirectView getProductImageByProductId(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "productImageId") Integer productImageId) {
		ProductImage productImage = mainService.getProductImage(productId, ProductImage.Type.TYPE_TH);
		return new RedirectView("/reserv/res/" + productImage.getSaveFileName());
	}
	
//=======================================================================
//메인 화면을 위한 컨트롤 끝
//=======================================================================
	
//=======================================================================
//상세 화면을 위한 컨트롤
//=======================================================================
	
	// http://localhost:8080/reserv/api/products/1
	@GetMapping("/products/{displayInfoId}")
	public Map<String, Object> DisplayInfoResponse
	(@PathVariable(name = "displayInfoId") Integer displayInfoId) 
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Double averageScore;
		List<Comment> comments;
		DisplayInfo displayInfo;
		DisplayInfoImage displayInfoImage;
		List<ProductImage> productImages;
		List<ProductPrice> productPrices;
		
		comments = detailService.getComments(displayInfoId);
		averageScore = detailService.getAverageScore(comments);
		displayInfo = detailService.getDisplayInfo(displayInfoId);
		displayInfoImage = detailService.getDisplayInfoImage(displayInfoId);
		productImages = detailService.getProductImages(displayInfo.getProductId());
		productPrices = detailService.getProductPrices(displayInfo.getProductId());
		
		map.put("averageScore", averageScore);
		map.put("comments", comments);
		map.put("displayInfo", displayInfo);
		map.put("displayInfoImage", displayInfoImage);
		map.put("productImages", productImages);
		map.put("productPrices", productPrices);
		return map;
	}
	
//=======================================================================
//상세 화면을 위한 컨트롤 끝
//=======================================================================
}

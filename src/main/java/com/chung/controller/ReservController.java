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

import com.chung.dto.category.Category;
import com.chung.dto.comment.Comment;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.dto.promotion.Promotion;
import com.chung.service.DetailService;
import com.chung.service.MainService;

@RestController
@RequestMapping(path = "/api")
public class ReservController {
	@Autowired
	MainService mainService;

	@Autowired
	DetailService detailService;

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
	public Map<String, Object> displayInfoResponse(@PathVariable(name = "displayInfoId") Integer displayInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();

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

//=======================================================================
//상세 화면을 위한 컨트롤 끝
//=======================================================================
}

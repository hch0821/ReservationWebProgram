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
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;
import com.chung.service.IService;

@RestController
@RequestMapping(path = "/api")
public class ReservController {
	@Autowired
	IService.Main mainService;

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
		List<ProductImage> productImages = mainService.getProductImage(productId, type);
		return new RedirectView("/reserv/res/img/" + productImages.get(0).getFileName());
	}

	// http://localhost:8080/reserv/api/productImages/{productId}/{productImageId}
	@GetMapping("/productImages/{productId}/{productImageId}")
	public RedirectView getProductImageByProductId(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "productImageId") Integer productImageId) {
		List<ProductImage> productImages = mainService.getProductImage(productId, ProductImage.Type.TYPE_TH);
		return new RedirectView("/reserv/res/img/" + productImages.get(0).getFileName());

	}
}

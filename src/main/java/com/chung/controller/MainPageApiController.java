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
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.promotion.Promotion;
import com.chung.service.impl.MainService;

// =======================================================================
// 메인 화면을 위한 API 컨트롤
// =======================================================================

@RestController
@RequestMapping(path = "/api")
public class MainPageApiController {
	@Autowired
	MainService mainService;

	@GetMapping("/products")
	public Map<String, Object> productResponse(@RequestParam(name = "categoryId", required = true) int categoryId,
			@RequestParam(name = "start", required = false, defaultValue = "0") int start) {
		List<Product> products = mainService.getProducts(categoryId, start);
		Map<String, Object> map = new HashMap<>();
		map.put("items", products);
		map.put("totalCount", mainService.getCategories().get(categoryId).getCount());
		return map;
	}

	@GetMapping("/categories")
	public Map<String, Object> categoryResponse() {
		List<Category> categories = mainService.getCategories();
		Map<String, Object> map = new HashMap<>();
		map.put("items", categories);
		return map;
	}

	@GetMapping("/promotions")
	public Map<String, Object> promotionResponse() {
		List<Promotion> promotions = mainService.getPromotions();
		Map<String, Object> map = new HashMap<>();
		map.put("items", promotions);
		return map;
	}

	@GetMapping("/productImages/{productId}")
	public RedirectView getProductImageByProductId(@PathVariable(name = "productId") Integer productId,
			@RequestParam(name = "type", required = true) String type) {
		ProductImage productImage = mainService.getProductImage(productId, type);
		return new RedirectView("/image?path=" + productImage.getSaveFileName());
	}

	@GetMapping("/productImages/{productId}/{productImageId}")
	public RedirectView getProductImageByProductImageId(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "productImageId") Integer productImageId) {
		ProductImage productImage = mainService.getProductImage(productId, ProductImage.Type.TYPE_TH);
		return new RedirectView("/image?path=" + productImage.getSaveFileName());
	}
}

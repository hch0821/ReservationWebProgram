package com.chung.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationParam;
import com.chung.dto.reservation.ReservationPrice;
import com.chung.service.impl.DetailService;
import com.chung.service.impl.MainService;
import com.chung.service.impl.ReservationService;

//예약 웹 프로그램 API 컨트롤러
@RestController
@RequestMapping(path = "/api")
public class ReservationApiController {
	@Autowired
	MainService mainService;

	@Autowired
	DetailService detailService;

	@Autowired
	ReservationService reservationService;

//=======================================================================
//메인 화면을 위한 API 컨트롤
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
//메인 화면을 위한 API 컨트롤 끝
//=======================================================================

//=======================================================================
//상세 화면을 위한 API 컨트롤
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
//상세 화면을 위한 API 컨트롤 끝
//=======================================================================

//=======================================================================
//예약 화면, 예약확인 화면을 위한 API 컨트롤
//=======================================================================	

	// 예약 조회
	// http://localhost:8080/reserv/api/reservations?reservationEmail=xxxx@naver.com
	@GetMapping("/reservations")
	public Map<String, Object> inquireReservationInfoResponse(
			@RequestParam(name = "reservationEmail", required = true) String reservationEmail) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReservationInfo> reservations = reservationService.inquireReservations(reservationEmail);
		int size = reservations.size();
		map.put("reservations", reservations);
		map.put("size", size);
		return map;
	}

	// 공연일자를 오늘을 포함하여 1~5일 뒤의 날짜로 설정
	// http://localhost:8080/reserv/api/reservations/reservationDate
	@GetMapping("/reservations/reservationDate")
	public Map<String, Object> getReservationDate() {
		Map<String, Object> map = new HashMap<String, Object>();

		Date reservationDate = new Date();
		String reservationDateStr = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(reservationDate);
		cal.add(Calendar.DATE, (new Random().nextInt(6)));
		reservationDate = new Date(cal.getTimeInMillis());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		reservationDateStr = simpleDateFormat.format(reservationDate);

		map.put("reservationDate", reservationDateStr);

		return map;
	}

	// 예약 하기
	// http://localhost:8080/reserv/api/reservations
	@PostMapping("/reservations")
	public Map<String, Object> makeReservationResponse(@RequestBody ReservationParam reservationParam) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> reservationResultMap = reservationService.makeReservation(reservationParam);
		List<ReservationPrice> prices = (List<ReservationPrice>) reservationResultMap.get("reservationPrices");
		ReservationInfo reservationInfo = (ReservationInfo) reservationResultMap.get("reservationInfo");
		map.put("cancelYn", reservationInfo.isCancelYn());
		map.put("createDate", reservationInfo.getCreateDate());
		map.put("displayInfoId", reservationInfo.getDisplayInfoId());
		map.put("modifyDate", reservationInfo.getModifyDate());
		map.put("prices", prices);
		map.put("productId", reservationInfo.getProductId());
		map.put("reservationDate", reservationInfo.getReservationDate());
		map.put("reservationEmail", reservationInfo.getReservationEmail());
		map.put("reservationInfoId", reservationInfo.getReservationInfoId());
		map.put("reservationName", reservationInfo.getReservationName());
		map.put("reservationTelephone", reservationInfo.getReservationTelephone());
		return map;
	}

	// 예약 취소
	// http://localhost:8080/reserv/api/reservations/{reservationInfoId}
	@PutMapping("/reservations/{reservationInfoId}")
	public Map<String, Object> cancelReservationResponse(
			@PathVariable(name = "reservationInfoId") Integer reservationInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> reservationCancelResultMap = reservationService.cancelReservation(reservationInfoId);

		List<ReservationPrice> prices = (List<ReservationPrice>) reservationCancelResultMap.get("reservationPrices");
		ReservationInfo reservationInfo = (ReservationInfo) reservationCancelResultMap.get("reservationInfo");
		map.put("cancelYn", reservationInfo.isCancelYn());
		map.put("createDate", reservationInfo.getCreateDate());
		map.put("displayInfoId", reservationInfo.getDisplayInfoId());
		map.put("modifyDate", reservationInfo.getModifyDate());
		map.put("prices", prices);
		map.put("productId", reservationInfo.getProductId());
		map.put("reservationDate", reservationInfo.getReservationDate());
		map.put("reservationEmail", reservationInfo.getReservationEmail());
		map.put("reservationInfoId", reservationInfo.getReservationInfoId());
		map.put("reservationName", reservationInfo.getReservationName());
		map.put("reservationTelephone", reservationInfo.getReservationTelephone());

		return map;
	}

//=======================================================================
//예약 화면, 예약확인 화면을 위한 API 컨트롤 끝
//=======================================================================		
}

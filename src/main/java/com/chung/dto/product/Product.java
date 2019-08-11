package com.chung.dto.product;

public class Product {
	private int displayInfoId; // 전시 id
	private String placeName; // 전시 장소명
	private String productContent; // 상품 상세 설명
	private String productDescription; // 상품 설명
	private String productId; // 상품
	private String productImageUrl; // 상품 썸네일 이미지 URL

	public int getDisplayInfoId() {
		return displayInfoId;
	}

	public void setDisplayInfoId(int displayInfoId) {
		this.displayInfoId = displayInfoId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getProductContent() {
		return productContent;
	}

	public void setProductContent(String productContent) {
		this.productContent = productContent;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	@Override
	public String toString() {
		return "Product [displayInfoId=" + displayInfoId + ", placeName=" + placeName + ", productContent="
				+ productContent + ", productDescription=" + productDescription + ", productId=" + productId
				+ ", productImageUrl=" + productImageUrl + "]";
	}
}

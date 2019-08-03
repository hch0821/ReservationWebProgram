package com.chung.dto;

public class Promotion {
	private Integer id; // 프로모션 id
	private Integer productId; // 상품 id
	private String productImageUrl; // 상품 썸네일 이미지 URL

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
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
		return "Promotion [id=" + id + ", productId=" + productId + ", productImageUrl=" + productImageUrl + "]";
	}

}

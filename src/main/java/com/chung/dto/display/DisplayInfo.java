package com.chung.dto.display;

//description:	상품 전시 (Display) 모델
public class DisplayInfo {

	private int categoryId;
	// 카테고리 (category) Id

	private String categoryName;
	// 카테고리 이름

	private String createDate;
	// 생성일

	private int displayInfoId;
	// 전시 (display_info) Id

	private String email;
	// 이메일

	private String homepage;
	// 홈페이지

	private String modifyDate;
	// 수정일

	private String openingHours;
	// 전시 시간

	private String placeLot;
	// 전시 번지명

	private String placeName;
	// 전시장

	private String placeStreet;
	// 전시 도로명

	private String productContent;
	// 상품 내용

	private String productDescription;
	// 상품 설명

	private String productEvent;
	// 상품 이벤트

	private int productId;
	// 상품 (product) Id

	private String telephone;

	// 전화번호
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getDisplayInfoId() {
		return displayInfoId;
	}

	public void setDisplayInfoId(int displayInfoId) {
		this.displayInfoId = displayInfoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}

	public String getPlaceLot() {
		return placeLot;
	}

	public void setPlaceLot(String placeLot) {
		this.placeLot = placeLot;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceStreet() {
		return placeStreet;
	}

	public void setPlaceStreet(String placeStreet) {
		this.placeStreet = placeStreet;
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

	public String getProductEvent() {
		return productEvent;
	}

	public void setProductEvent(String productEvent) {
		this.productEvent = productEvent;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "DisplayInfo [categoryId=" + categoryId + ", categoryName=" + categoryName + ", createDate=" + createDate
				+ ", displayInfoId=" + displayInfoId + ", email=" + email + ", homepage=" + homepage + ", modifyDate="
				+ modifyDate + ", openingHours=" + openingHours + ", placeLot=" + placeLot + ", placeName=" + placeName
				+ ", placeStreet=" + placeStreet + ", productContent=" + productContent + ", productDescription="
				+ productDescription + ", productEvent=" + productEvent + ", productId=" + productId + ", telephone="
				+ telephone + "]";
	}

}

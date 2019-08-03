package com.chung.dto;

public class ProductImage {
	public class Type {
		public final static String TYPE_MA = "ma";
		public final static String TYPE_TH = "th";
		public final static String TYPE_ET = "et";
	}

	private String contentType;
//	파일 확장자

	private String createDate;
//	생성일

	private boolean deleteFlag;
//	삭제 여부

	private int fileInfoId;
//	파일 Id

	private String fileName;
//	파일 이름

	private String modifyDate;
//	수정일

	private int productId;
//	상품 Id

	private int productImageId;
//	상품 이미지 Id

	private String saveFileName;
//	파일 저장 위치 이름

	private String type;
//	이미지 타입 (main, thumbnail, etc)

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public int getFileInfoId() {
		return fileInfoId;
	}

	public void setFileInfoId(int fileInfoId) {
		this.fileInfoId = fileInfoId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductImageId() {
		return productImageId;
	}

	public void setProductImageId(int productImageId) {
		this.productImageId = productImageId;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ProductImage [contentType=" + contentType + ", createDate=" + createDate + ", deleteFlag=" + deleteFlag
				+ ", fileInfoId=" + fileInfoId + ", fileName=" + fileName + ", modifyDate=" + modifyDate
				+ ", productId=" + productId + ", productImageId=" + productImageId + ", saveFileName=" + saveFileName
				+ ", type=" + type + "]";
	}

}

package com.chung.dto.comment;

import java.io.File;

public class CommentParam {

	private File attachedImage;
	private String comment;
	private int productId;
	private int reservationInfoId;
	private int score;
	public File getAttachedImage() {
		return attachedImage;
	}
	public void setAttachedImage(File attachedImage) {
		this.attachedImage = attachedImage;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getReservationInfoId() {
		return reservationInfoId;
	}
	public void setReservationInfoId(int reservationInfoId) {
		this.reservationInfoId = reservationInfoId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "CommentParam [attachedImage=" + attachedImage + ", comment=" + comment + ", productId=" + productId
				+ ", reservationInfoId=" + reservationInfoId + ", score=" + score + "]";
	}
	
}

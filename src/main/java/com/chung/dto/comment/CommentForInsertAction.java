package com.chung.dto.comment;

import java.util.Date;

public class CommentForInsertAction {
	private int productId;
	private int reservationInfoId;
	private double score;
	private String comment;
	private Date createDate;
	private Date modifyDate;

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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "CommentForInsertAction [productId=" + productId + ", reservationInfoId=" + reservationInfoId
				+ ", score=" + score + ", comment=" + comment + ", createDate=" + createDate + ", modifyDate="
				+ modifyDate + "]";
	}

}

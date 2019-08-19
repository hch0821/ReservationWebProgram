package com.chung.dto.reservation;

import java.util.Date;

//예약 정보 insert query 작업을 위한 DTO
public class ReservationInfoForInsertAction {
	private int displayInfoId;
	private int productId;
	private String reservationEmail;
	private String reservationName;
	private String reservationTel;
	private Date reservationDate;
	private int cancelFlag;
	private Date createDate;
	private Date modifyDate;
	
	public ReservationInfoForInsertAction(int displayInfoId, int productId, String reservationEmail, String reservationName,
			String reservationTel, Date reservationDate, int cancelFlag, Date createDate, Date modifyDate) {
		this.displayInfoId = displayInfoId;
		this.productId = productId;
		this.reservationEmail = reservationEmail;
		this.reservationName = reservationName;
		this.reservationTel = reservationTel;
		this.reservationDate = reservationDate;
		this.cancelFlag = cancelFlag;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}

	public int getDisplayInfoId() {
		return displayInfoId;
	}

	public void setDisplayInfoId(int displayInfoId) {
		this.displayInfoId = displayInfoId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getReservationEmail() {
		return reservationEmail;
	}

	public void setReservationEmail(String reservationEmail) {
		this.reservationEmail = reservationEmail;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	public String getReservationTel() {
		return reservationTel;
	}

	public void setReservationTel(String reservationTel) {
		this.reservationTel = reservationTel;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public int getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(int cancelFlag) {
		this.cancelFlag = cancelFlag;
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
		return "ReservationInfoForInsertAction [displayInfoId=" + displayInfoId + ", productId=" + productId
				+ ", reservationEmail=" + reservationEmail + ", reservationName=" + reservationName
				+ ", reservationTel=" + reservationTel + ", reservationDate=" + reservationDate + ", cancelFlag="
				+ cancelFlag + ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}
	
	

}

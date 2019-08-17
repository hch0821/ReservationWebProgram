package com.chung.dto.reservation;

//예약가격 내역
public class ReservationPrice {
	private int count;
	//예약 상품 수

	private int productPriceId;
	//상품 가격 Id

	private int reservationInfoId;
	//예약 Id


	public ReservationPrice(int count, int productPriceId, int reservationInfoId) {
		this.count = count;
		this.productPriceId = productPriceId;
		this.reservationInfoId = reservationInfoId;
	}
	
	public int getCount() {
		return count;
	}

	
	public void setCount(int count) {
		this.count = count;
	}

	public int getProductPriceId() {
		return productPriceId;
	}

	public void setProductPriceId(int productPriceId) {
		this.productPriceId = productPriceId;
	}

	public int getReservationInfoId() {
		return reservationInfoId;
	}

	public void setReservationInfoId(int reservationInfoId) {
		this.reservationInfoId = reservationInfoId;
	}


	@Override
	public String toString() {
		return "ReservationPrice [count=" + count + ", productPriceId=" + productPriceId + ", reservationInfoId="
				+ reservationInfoId + "]";
	}
	
	
	
}

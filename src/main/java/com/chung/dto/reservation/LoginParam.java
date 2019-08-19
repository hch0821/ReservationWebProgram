package com.chung.dto.reservation;

//로그인 폼에 있는 값을 받기 위한 클래스
public class LoginParam {
	private String reservationEmail;

	public String getReservationEmail() {
		return reservationEmail;
	}

	public void setReservationEmail(String reservationEmail) {
		this.reservationEmail = reservationEmail;
	}

	@Override
	public String toString() {
		return "LoginParam [reservationEmail=" + reservationEmail + "]";
	}

}

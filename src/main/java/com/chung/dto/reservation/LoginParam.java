package com.chung.dto.reservation;

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

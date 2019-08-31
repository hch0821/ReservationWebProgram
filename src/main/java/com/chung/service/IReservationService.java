package com.chung.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationParam;

@Service
public interface IReservationService {
	public List<ReservationInfo> inquireReservations(String reservationEmail); // 예약조회

	public Map<String, Object> reserveTicket(ReservationParam reservationParam); // 예약하기

	public Map<String, Object> cancelReservation(int reservationInfoId); // 예약 취소

}

package com.chung.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationParam;
import com.chung.service.impl.ReservationService;

//=======================================================================
//예약 화면, 예약확인 화면을 위한 API 컨트롤
//=======================================================================	

@RestController
@RequestMapping(path = "/api")
public class ReservationPageApiController {
	@Autowired
	ReservationService reservationService;

	// 예약 조회
	// http://localhost:8080/reserv/api/reservations?reservationEmail=xxxx@naver.com
	@GetMapping("/reservations")
	public Map<String, Object> inquireReservationInfoResponse(
			@RequestParam(name = "reservationEmail", required = true) String reservationEmail) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReservationInfo> reservations = reservationService.inquireReservations(reservationEmail);
		int size = reservations.size();
		map.put("reservations", reservations);
		map.put("size", size);
		return map;
	}

	// 공연일시를 오늘을 포함하여 1~5일 뒤의 날짜로 설정
	// http://localhost:8080/reserv/api/reservations/reservationDate
	@GetMapping("/reservations/reservationDate")
	public Map<String, Object> getReservationDate() {
		Map<String, Object> map = new HashMap<>();

		Date reservationDate = new Date();
		String reservationDateStr = "";
		int addedDate = (new Random().nextInt(6)); // 오늘로 부터 0 ~ 5일이 지난 일시로 설정
		System.out.println(addedDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reservationDate);
		cal.add(Calendar.DATE, addedDate);
		
		if(addedDate == 0) { // 랜덤으로 잡힌 공연일시가 오늘일 때 
			
			cal.add(Calendar.HOUR_OF_DAY, new Random().nextInt(5)+1); // 오늘 시간보다 더 이전 시간으로 잡히는 것을 방지
		}else {
			cal.set(Calendar.HOUR_OF_DAY, new Random().nextInt(10) + 9); //오전 9시 ~ 오후 6시로 시간 설정
		}
		cal.set(Calendar.MINUTE, new Random().nextInt(2) * 30); //분은 00분 또는 30분으로 설정
		reservationDate = new Date(cal.getTimeInMillis());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		reservationDateStr = simpleDateFormat.format(reservationDate);

		map.put("reservationDate", reservationDateStr);

		return map;
	}

	// 예약 하기
	// http://localhost:8080/reserv/api/reservations
	@PostMapping("/reservations")
	public Map<String, Object> reserveTicketResponse(@RequestBody ReservationParam reservationParam) {

		return reservationService.reserveTicket(reservationParam);
	}

	// 예약 취소
	// http://localhost:8080/reserv/api/reservations/{reservationInfoId}
	@PutMapping("/reservations/{reservationInfoId}")
	public Map<String, Object> cancelReservationResponse(
			@PathVariable(name = "reservationInfoId") Integer reservationInfoId) {

		return reservationService.cancelReservation(reservationInfoId);
	}
}

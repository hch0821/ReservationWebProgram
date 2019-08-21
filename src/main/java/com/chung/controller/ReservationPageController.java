package com.chung.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chung.dto.reservation.LoginParam;

// API를 제외한 기능을 가지고 있는 컨트롤러
@Controller
public class ReservationPageController {
//======================================================================
//예약 로그인 화면을 위한 컨트롤
//=======================================================================	

	// 세션이 살아있는지 확인하는 함수
	// http://localhost:8080/reserv/checkSession
	@GetMapping(path = "/checkSession")
	public String checkSessionIsAlive(HttpSession session) {
		Object reservationEmail = session.getAttribute("reservationEmail");
		if (reservationEmail != null && !reservationEmail.toString().equals("")) {
			return "redirect:/myreservation";
		} else {
			return "redirect:/bookinglogin";
		}
	}

	// 로그인 요청이 들어왔을 때 세션에 이메일을 저장하는 함수
	// http://localhost:8080/reserv/loginrequest
	@PostMapping(path = "/loginrequest")
	public String loginSite(@ModelAttribute LoginParam loginParam, HttpSession session) {
		String reservationEmail = loginParam.getReservationEmail();
		session.setAttribute("reservationEmail", reservationEmail);

		return "redirect:/myreservation";
	}

	// 로그아웃 요청이 들어왔을 때 세션에 있는 이메일 정보를 삭제하는 함수
	// http://localhost:8080/reserv/logoutrequest
	@GetMapping(path = "/logoutrequest")
	public String logoutSite(HttpSession session) {
		session.removeAttribute("reservationEmail");
		return "redirect:/bookinglogin";
	}

//=======================================================================
//예약 로그인 화면을 위한 컨트롤
//=======================================================================	
}

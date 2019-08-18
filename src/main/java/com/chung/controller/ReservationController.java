package com.chung.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chung.dto.reservation.LoginParam;

@Controller
public class ReservationController 
{
	
	@GetMapping(path="/checkSession")
	public String checkSessionIsAlive(HttpSession session)
	{
		Object reservationEmail = session.getAttribute("reservationEmail");
		if(reservationEmail != null && !reservationEmail.toString().equals(""))
		{
			System.out.println("로그인 되어있음.");
			return "redirect:/myreservation";
		}
		else {
			System.out.println("로그인 필요.");
			return "redirect:/bookinglogin";
		}
	}
	
	@PostMapping(path="/loginrequest")
	public String login(@ModelAttribute LoginParam loginParam,
			HttpSession session) 
	{
		String reservationEmail = loginParam.getReservationEmail();
		System.out.println(reservationEmail);
	
		session.setAttribute("reservationEmail", reservationEmail);

		return "redirect:/myreservation"; 
	}
	
	@GetMapping(path="/logoutrequest")
	public String logout(HttpSession session) {
		session.removeAttribute("reservationEmail");
		return "redirect:/";
	}
}

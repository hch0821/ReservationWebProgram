package com.chung.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chung.service.impl.DetailService;

@Controller
public class DetailPageController {
	@Autowired
	DetailService detailService;
	
	@GetMapping(path= "/commentimage")
	public String getCommentImage(@RequestParam(required=true, name="id") int id) {
		return "redirect:image?path=" + detailService.lookupFilepathByReservationUserCommentImageId(id);
	}
}

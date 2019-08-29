package com.chung.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chung.dto.fileinfo.FileInfo;
import com.chung.service.IImageFileService;
import com.chung.service.impl.ReviewService;

@RestController
@RequestMapping(path = "/api")
public class ReviewPageApiController {
	
	@Autowired
	ReviewService reviewService;
	
	@PostMapping(value = "/reservations/{reservationInfoId}/comments")		
	public Map<String, Object> getCommentResponse(
			@PathVariable(required = true, name = "reservationInfoId") int reservationInfoId, 
			@RequestParam(value="attachedImage", required=false) List<MultipartFile> attachedImages,
			@RequestParam(value="comment", required=true) String comment,
			@RequestParam(value="productId", required=true) int productId,
			@RequestParam(value="score", required=true) double score)
	{
		System.out.println(attachedImages);
		System.out.println(comment);
		System.out.println(reservationInfoId);
		System.out.println(score);
		int reservationUserCommentId = (int)reviewService.registerComment(productId, reservationInfoId, score, comment);
		
		for(MultipartFile attachedImage : attachedImages) {
			if(attachedImage.getSize()== 0)
			{
				continue;
			}
			FileInfo uploadedFileInfo = reviewService.uploadCommentImageFile(attachedImage, IImageFileService.COMMENT_IMAGE_SUB_DIRECTORY, true);	
			long commentImageId = reviewService.registerCommentImage(reservationInfoId, reservationUserCommentId, uploadedFileInfo.getId());
			System.out.println(uploadedFileInfo);
			System.out.println("commentImageId : " + commentImageId);
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("success", true);
		
		return map;
	}
}

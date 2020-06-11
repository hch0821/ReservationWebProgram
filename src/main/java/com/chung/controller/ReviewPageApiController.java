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

import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentImage;
import com.chung.dto.fileinfo.FileInfo;
import com.chung.service.impl.ReviewService;

//리뷰 쓰기 페이지를 위한 컨트롤러
@RestController
@RequestMapping(path = "/api")
public class ReviewPageApiController {

	@Autowired
	ReviewService reviewService;

	// 리뷰 등록 또는 수정
	@PostMapping(value = "/reservations/{reservationInfoId}/comments")
	public Map<String, Object> getReviewResponse(
			@PathVariable(required = true, name = "reservationInfoId") int reservationInfoId,
			@RequestParam(value = "attachedImage", required = false) List<MultipartFile> attachedImage,
			@RequestParam(value = "comment", required = true) String comment,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "score", required = true) int score,
			@RequestParam(value = "isOriginImageExists", required = true) boolean isOriginImageExists) {

		List<Comment> comments = reviewService.getCommentsByReservationInfoId(reservationInfoId);

		// 신규 리뷰 작성
		if (comments == null) {
			addNewReview(productId, reservationInfoId, score, comment, attachedImage);
		}

		// 리뷰 수정
		else {
			Comment originalComment = comments.get(0);
			modifyOriginalReview(originalComment, comment, score, attachedImage, isOriginImageExists);
		}

		return getResultReviewMap(reservationInfoId);
	}

	// 신규 리뷰 작성
	private void addNewReview(int productId, int reservationInfoId, int score, String commentStr,
			List<MultipartFile> attachedImages) {
		int reservationUserCommentId = (int) reviewService.registerCommentAndScore(productId, reservationInfoId, score,
				commentStr);
		for (MultipartFile attachedImage : attachedImages) {
			if (attachedImage.getSize() == 0) {
				continue;
			}
			FileInfo uploadedFileInfo = reviewService.uploadCommentImageFile(attachedImage, true);
			reviewService.registerCommentImage(reservationInfoId, reservationUserCommentId, uploadedFileInfo.getId());
		}
	}

	// 리뷰 수정
	private void modifyOriginalReview(Comment originalComment, String newCommentStr, int newScore,
			List<MultipartFile> newAttachedImages, boolean isOriginImageExists) {
		int reservationUserCommentId = originalComment.getCommentId();
		int reservationInfoId = originalComment.getReservationInfoId();

		// 댓글 수정
		if (!originalComment.getComment().equals(newCommentStr)) {
			reviewService.updateComment(newCommentStr, reservationUserCommentId);
		}

		// 점수 수정
		if (originalComment.getScore() != newScore) {
			reviewService.updateScore(newScore, reservationUserCommentId);
		}

		// 이전 이미지가 그대로 보존되어있을 경우
		if (isOriginImageExists) {
			return;
		}

		// 아래 코드부터는 이전 이미지가 사라지고 다른 이미지로 대체되거나 아예 첨부파일이 없는 경우의 작업.

		List<CommentImage> originalCommentImages = originalComment.getCommentImages();
		// 아무 이미지가 올라오지 않았을 경우
		// deleteFlag가 false인 commentImage를 찾아서 deleteFlag를 true로 바꾸고 해당 파일을 삭제함.
		if (newAttachedImages.size() == 0
				|| (newAttachedImages.size() == 1 && newAttachedImages.get(0).getSize() == 0)) {
			for (CommentImage originalCommentImage : originalCommentImages) {
				if (!originalCommentImage.isDeleteFlag()) {
					deleteCommentImage(originalCommentImage);
				}
			}
			return;
		}

		// 이미지가 한 개라도 올라왔다면
		for (MultipartFile attachedImage : newAttachedImages) {
			if (attachedImage.getSize() == 0) {
				continue;
			}

			// 기존에 있던 이미지와 동일한 이미지가 아니라면 해당 이미지의 deleteFlag를 true로 바꾸고 해당 파일을 삭제함.
			for (CommentImage originalCommentImage : originalCommentImages) {
				if (!originalCommentImage.isDeleteFlag()
						&& !originalCommentImage.getFileName().equals(attachedImage.getOriginalFilename())) {
					deleteCommentImage(originalCommentImage);
				}
			}

			// 기존에 있던 이미지와 동일한 이미지가 아니라면
			for (CommentImage originalCommentImage : originalCommentImages) {
				if (!originalCommentImage.isDeleteFlag()
						&& originalCommentImage.getFileName().equals(attachedImage.getOriginalFilename())) {
					return; // 동일한 이미지(이미 존재하는 이미지)이면 return
				}
			}

			// 파일을 업로드하고 새로 db에 이미지를 등록함 .
			FileInfo uploadedFileInfo = reviewService.uploadCommentImageFile(attachedImage, true);
			reviewService.registerCommentImage(reservationInfoId, reservationUserCommentId, uploadedFileInfo.getId());

		}
	}

	// 이미지의 deleteFlag를 true로 만들고 실제 파일까지 삭제하는 함수
	private void deleteCommentImage(CommentImage commentImage) {
		// update originalCommentImage -> delete flag = 1
		reviewService.updateDeleteFlagOfCommentImageFile(1, commentImage.getImageId());

		// delete commentImage file
		reviewService.deleteCommentImageFile(commentImage);

	}

	private Map<String, Object> getResultReviewMap(int reservationInfoId) {
		List<Comment> resultComments = reviewService.getCommentsByReservationInfoId(reservationInfoId);
		Map<String, Object> map = new HashMap<>();
		if (resultComments == null || resultComments.size() == 0) {
			throw new RuntimeException("Cannot find comments.");
		}
		Comment resultComment = resultComments.get(0);

		map.put("comment", resultComment.getComment());
		map.put("commentId", resultComment.getCommentId());
		map.put("commentImage", resultComment.getCommentImages());
		map.put("createDate", resultComment.getCreateDate());
		map.put("modifyDate", resultComment.getModifyDate());
		map.put("productId", resultComment.getProductId());
		map.put("reservationInfoId", resultComment.getReservationInfoId());
		map.put("score", resultComment.getScore());
		return map;
	}

}

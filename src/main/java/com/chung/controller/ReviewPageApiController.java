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

import com.chung.dao.FileDao;
import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentImage;
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
			@RequestParam(value = "attachedImage", required = false) List<MultipartFile> attachedImages,
			@RequestParam(value = "comment", required = true) String commentStr,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "score", required = true) double score) {

		List<Comment> comments = reviewService.getCommentsByReservationInfoId(reservationInfoId);

		// 신규 댓글 작성
		if (comments == null) {
			int reservationUserCommentId = (int) reviewService.registerComment(productId, reservationInfoId, score,
					commentStr);
			for (MultipartFile attachedImage : attachedImages) {
				if (attachedImage.getSize() == 0) {
					continue;
				}
				FileInfo uploadedFileInfo = reviewService.uploadCommentImageFile(attachedImage,
						IImageFileService.COMMENT_IMAGE_SUB_DIRECTORY, true);
				reviewService.registerCommentImage(reservationInfoId, reservationUserCommentId,
						uploadedFileInfo.getId());

			}

		}

		// 댓글 수정
		else {
			Comment originalComment = comments.get(0);
			int reservationUserCommentId = originalComment.getCommentId();

			// update comment
			if (!originalComment.getComment().equals(commentStr)
					&& !reviewService.updateComment(commentStr, reservationUserCommentId)) {
				throw new RuntimeException("Cannot update comment.");
			}

			// update score
			if (originalComment.getScore() != score && !reviewService.updateScore(score, reservationUserCommentId)) {
				throw new RuntimeException("Cannot update score");
			}

			List<CommentImage> originalCommentImages = originalComment.getCommentImages();
			for (MultipartFile attachedImage : attachedImages) {
				if (attachedImage.getSize() == 0) {
					continue;
				}
				for (CommentImage originalCommentImage : originalCommentImages) {
					if (!originalCommentImage.isDeleteFlag()
							&& !originalCommentImage.getFileName().equals(attachedImage.getOriginalFilename())) {
						// update originalCommentImage - delete flag == 1
						if (!reviewService.updateDeleteFlagOfImageFile(1, originalCommentImage.getImageId())) {
							throw new RuntimeException("Cannot update delete flag of comment image.");
						}

						// insert new attachedImage
						FileInfo uploadedFileInfo = reviewService.uploadCommentImageFile(attachedImage,
								IImageFileService.COMMENT_IMAGE_SUB_DIRECTORY, true);
						reviewService.registerCommentImage(reservationInfoId,
								reservationUserCommentId, uploadedFileInfo.getId());
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
}

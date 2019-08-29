package com.chung.service;

import java.util.List;

import com.chung.dto.comment.Comment;

public interface IRateInquirementService {
	public Double getAverageScore(List<Comment> comments);

	public List<Comment> getComments(int displayInfoId);
	
	public String lookupFilepathByReservationUserCommentImageId(int reservationUserCommentImageId);
}

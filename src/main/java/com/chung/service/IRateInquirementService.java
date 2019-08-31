package com.chung.service;

import java.util.List;

import com.chung.dto.comment.Comment;

public interface IRateInquirementService {
	public Double getAverageScore(List<Comment> comments); //평균 점수 조회

	public List<Comment> getComments(int displayInfoId);  // displayInfoId를 가지고 댓글 목록 조회
	
	public String lookupFilepathByReservationUserCommentImageId(int reservationUserCommentImageId); //댓글이미지 id를 가지고 이미지 파일 경로 조회
}

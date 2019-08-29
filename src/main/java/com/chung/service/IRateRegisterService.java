package com.chung.service;

import java.util.List;

import com.chung.dto.comment.Comment;

public interface IRateRegisterService {
	public long registerComment(int productId, int reservationInfoId, double score, String comment);
	public long registerCommentImage(int reservationInfoId, int reservationUserCommentId, int fileId);
	public List<Comment> getCommentsByReservationInfoId(int reservationInfoId);
	public boolean updateScore(int score, int reservationUserCommentId);
	public boolean updateComment(String comment, int reservationUserCommentId);
}

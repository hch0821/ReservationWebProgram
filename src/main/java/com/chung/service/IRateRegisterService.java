package com.chung.service;

public interface IRateRegisterService {
	public long registerComment(int productId, int reservationInfoId, double score, String comment);
	public long registerCommentImage(int reservationInfoId, int reservationUserCommentId, int fileId);
}

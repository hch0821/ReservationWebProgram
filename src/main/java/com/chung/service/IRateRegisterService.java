package com.chung.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentImage;
import com.chung.dto.fileinfo.FileInfo;

//상품 평가 등록 서비스
public interface IRateRegisterService {
	public long registerCommentAndScore(int productId, int reservationInfoId, int score, String comment); // 댓글, 점수 등록

	public long registerCommentImage(int reservationInfoId, int reservationUserCommentId, int fileId); // 댓글 이미지 등록

	public List<Comment> getCommentsByReservationInfoId(int reservationInfoId); // 예약 id로 댓글 찾기

	public void updateScore(int score, int reservationUserCommentId); // 점수 수정

	public void updateComment(String comment, int reservationUserCommentId); // 댓글 수정

	public FileInfo uploadCommentImageFile(MultipartFile imagefile, boolean hasDateFolder); // 댓글 이미지 수정

	public void updateDeleteFlagOfCommentImageFile(int deleteFlag, int reservationUserCommentImageId); // 댓글 이미지의  delete flag 수정

	boolean deleteCommentImageFile(CommentImage commentImage); // 댓글 이미지 파일 삭제
}

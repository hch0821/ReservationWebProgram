package com.chung.dto.comment;

public class CommentImageForInsertAction {
	private int reservationInfoId;
	private int reservationUserCommentId;
	private int fileId;

	public int getReservationInfoId() {
		return reservationInfoId;
	}

	public void setReservationInfoId(int reservationInfoId) {
		this.reservationInfoId = reservationInfoId;
	}

	public int getReservationUserCommentId() {
		return reservationUserCommentId;
	}

	public void setReservationUserCommentId(int reservationUserCommentId) {
		this.reservationUserCommentId = reservationUserCommentId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	@Override
	public String toString() {
		return "CommentImageForInsertAction [reservationInfoId=" + reservationInfoId + ", reservationUserCommentId="
				+ reservationUserCommentId + ", fileId=" + fileId + "]";
	}

}

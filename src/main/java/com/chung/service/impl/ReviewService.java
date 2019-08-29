package com.chung.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chung.dao.CommentDao;
import com.chung.dao.FileDao;
import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentForInsertAction;
import com.chung.dto.comment.CommentImageForInsertAction;
import com.chung.dto.fileinfo.FileInfo;
import com.chung.service.IImageFileService;
import com.chung.service.IRateRegisterService;

@Service
public class ReviewService implements IRateRegisterService, IImageFileService {

	@Autowired
	CommentDao commentDao;

	@Autowired
	FileDao fileDao;

	public ReviewService() {
		File rootDirectory = new File(ROOT_DIRECTORY);
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}
	}

	@Override
	public FileInfo uploadCommentImageFile(MultipartFile sourceFile, String subDirectoryPath, boolean hasDateFolder) {
		FileInfo fileInfo = new FileInfo();
		System.out.println("업로드 파일 이름: " + sourceFile.getOriginalFilename());
		System.out.println("파일 크기: " + sourceFile.getSize());
		File destDirectory = new File(ROOT_DIRECTORY, subDirectoryPath);
		if (!destDirectory.exists() && !destDirectory.mkdirs()) {
			return null;
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HH.mm.ss.SSSS");
		String todayDatetimeStr = sdf.format(date);

		String[] fileNameAndExtension = getFilenameAndExtension(sourceFile.getOriginalFilename());
		String destFileName = todayDatetimeStr + fileNameAndExtension[1];
		String todayDateStr = "";
		if (hasDateFolder) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			todayDateStr = sdf.format(date);
			destDirectory = new File(destDirectory, todayDateStr);
			if (!destDirectory.exists() && !destDirectory.mkdirs()) {
				return null;
			}
		}
		File destFile = new File(destDirectory, destFileName);

		if (!readFile(sourceFile, destFile)) {
			return null;
		}

		String uploadFilePath = destFile.getAbsolutePath();
		fileInfo.setContentType(URLConnection.guessContentTypeFromName(uploadFilePath));
		if (hasDateFolder) {
			uploadFilePath = subDirectoryPath + "/" + todayDateStr + "/" + destFile.getName();
		} else {
			uploadFilePath = subDirectoryPath + "/" + destFile.getName();
		}

		fileInfo.setCreateDate(date);
		fileInfo.setModifyDate(date);

		fileInfo.setDeleteFlag(false);
		fileInfo.setFileName(destFileName);
		fileInfo.setSaveFileName(uploadFilePath);

		int fileId = (int) fileDao.insertFileInfo(fileInfo);
		fileInfo.setId(fileId);

		return fileInfo;
	}

	@Override
	public long registerComment(int productId, int reservationInfoId, double score, String comment) {

		Date date = new Date();
		CommentForInsertAction commentForInsertAction = new CommentForInsertAction();
		commentForInsertAction.setProductId(productId);
		commentForInsertAction.setReservationInfoId(reservationInfoId);
		commentForInsertAction.setScore(score);
		commentForInsertAction.setComment(comment);
		commentForInsertAction.setCreateDate(date);
		commentForInsertAction.setModifyDate(date);
		return commentDao.insertReservationUserComment(commentForInsertAction);
	}

	@Override
	public long registerCommentImage(int reservationInfoId, int reservationUserCommentId, int fileId) {
		CommentImageForInsertAction commentImageForInsertAction = new CommentImageForInsertAction();
		commentImageForInsertAction.setReservationInfoId(reservationInfoId);
		commentImageForInsertAction.setReservationUserCommentId(reservationUserCommentId);
		commentImageForInsertAction.setFileId(fileId);
		return commentDao.insertReservationUserCommentImage(commentImageForInsertAction);
	}

	private boolean readFile(MultipartFile sourceFile, File destFile) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(destFile);
			is = sourceFile.getInputStream();
			int readCount = 0;
			byte[] buffer = new byte[1024];
			while ((readCount = is.read(buffer)) != -1) {
				fos.write(buffer, 0, readCount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}

		}
		return true;
	}

	private String[] getFilenameAndExtension(String fileName) {
		String[] nameAndExtension = { "", "" };
		int idx = fileName.lastIndexOf('.');
		if (idx != -1) {
			nameAndExtension[0] = fileName.substring(0, idx);
			nameAndExtension[1] = fileName.substring(idx, fileName.length());
		} else {
			nameAndExtension[0] = fileName;
		}

		return nameAndExtension;
	}

	@Override
	public List<Comment> getCommentsByReservationInfoId(int reservationInfoId) {
		List<Comment> comments = commentDao.selectCommentsByReservationInfoId(reservationInfoId);
		if (comments == null || comments.size() == 0)
			return null;

		for (Comment c : comments) {
			c.setCommentImages(commentDao.selectCommentImages(c.getCommentId()));
		}
		return comments;
	}

	@Override
	public boolean updateDeleteFlagOfImageFile(int deleteFlag, int reservationUserCommentImageId) {
		return fileDao.updateDeleteFlagOfFileInfo(deleteFlag, reservationUserCommentImageId) == 1;
	}

	@Override
	public boolean updateScore(double score, int reservationUserCommentId) {
		return commentDao.updateScoreOfReservationUserComment(score, reservationUserCommentId) == 1;
	}

	@Override
	public boolean updateComment(String comment, int reservationUserCommentId) {
		return commentDao.updateCommentOfReservationUserComment(comment, reservationUserCommentId) == 1;
	}

}

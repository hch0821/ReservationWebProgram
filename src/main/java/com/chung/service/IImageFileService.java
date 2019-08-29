package com.chung.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chung.dto.fileinfo.FileInfo;

public interface IImageFileService {
	public final static String ROOT_DIRECTORY = "D:/ReservationWebProgram";
	public final static String COMMENT_IMAGE_SUB_DIRECTORY = "commentImages";
	public FileInfo uploadCommentImageFile(MultipartFile imagefile, String subDirectoryPath, boolean hasDateFolder);
}

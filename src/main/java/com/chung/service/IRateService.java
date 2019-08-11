package com.chung.service;

import java.util.List;

import com.chung.dto.comment.Comment;

public interface IRateService {
	public Double getAverageScore(List<Comment> comments);

	public List<Comment> getComments(int displayInfoId);
}

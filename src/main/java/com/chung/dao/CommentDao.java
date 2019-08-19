package com.chung.dao;

import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENTS;
import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENT_IMAGES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentImage;

@Repository
public class CommentDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Comment> commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);
	private RowMapper<CommentImage> commentImageMapper = BeanPropertyRowMapper.newInstance(CommentImage.class);
	private CommentDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
	public List<Comment> selectComments(Integer displayInfoId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("displayInfoId", displayInfoId);
		return jdbc.query(SELECT_COMMENTS, params, commentMapper);
	}

	public List<CommentImage> selectCommentImages(Integer commentId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("commentId", commentId);

		return jdbc.query(SELECT_COMMENT_IMAGES, params, commentImageMapper);
	}
}

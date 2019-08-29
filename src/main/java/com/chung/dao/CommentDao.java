package com.chung.dao;

import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENTS;
import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENT_IMAGES;
import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENT_IMAGE_NAME_BY_COMMENT_IMAGE_ID;
import static com.chung.dao.ReservationProgramSqls.UPDATE_COMMENT_OF_RESERVATION_USER_COMMENT;
import static com.chung.dao.ReservationProgramSqls.UPDATE_SCORE_OF_RESERVATION_USER_COMMENT;
import static com.chung.dao.ReservationProgramSqls.SELECT_COMMENT_BY_RESERVATION_INFO_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentForInsertAction;
import com.chung.dto.comment.CommentImage;
import com.chung.dto.comment.CommentImageForInsertAction;

@Repository
public class CommentDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Comment> commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);
	private RowMapper<CommentImage> commentImageMapper = BeanPropertyRowMapper.newInstance(CommentImage.class);
	
	private SimpleJdbcInsert insertReservationUserCommentImageAction;
	private SimpleJdbcInsert insertReservationUserCommentAction;
	private CommentDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
		
		insertReservationUserCommentImageAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_user_comment_image")
				.usingGeneratedKeyColumns("id");
		insertReservationUserCommentAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_user_comment")
				.usingGeneratedKeyColumns("id");
	}
	public List<Comment> selectCommentsDisplayInfoId(Integer displayInfoId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("displayInfoId", displayInfoId);
		return jdbc.query(SELECT_COMMENTS, params, commentMapper);
	}
	
	public List<Comment> selectCommentsByReservationInfoId(Integer reservationInfoId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("reservationInfoId", reservationInfoId);
		
		return jdbc.query(SELECT_COMMENT_BY_RESERVATION_INFO_ID, params, commentMapper);
		
	}

	public List<CommentImage> selectCommentImages(Integer commentId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("commentId", commentId);

		return jdbc.query(SELECT_COMMENT_IMAGES, params, commentImageMapper);
	}
	
	public String selectCommentImagenameByCommentImageId(Integer reservationUserCommentImageId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("reservationUserCommentImageId", reservationUserCommentImageId);
		return jdbc.queryForObject(SELECT_COMMENT_IMAGE_NAME_BY_COMMENT_IMAGE_ID, params, String.class);
	}
	
	public Long insertReservationUserCommentImage(CommentImageForInsertAction commentImageForInsertAction) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(commentImageForInsertAction);
		return insertReservationUserCommentImageAction.executeAndReturnKey(params).longValue();
	}
	
	public Long insertReservationUserComment(CommentForInsertAction commentForInsertAction) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(commentForInsertAction);
		return insertReservationUserCommentAction.executeAndReturnKey(params).longValue();
	}
	
	public int updateScoreOfReservationUserComment(int score, int reservationUserCommentId) {
		Map<String, Integer> params = new HashMap<>();
		params.put("score", score);
		params.put("reservationUserCommentId", reservationUserCommentId);
		return jdbc.update(UPDATE_SCORE_OF_RESERVATION_USER_COMMENT, params);
	}
	
	public int updateCommentOfReservationUserComment(String comment, int reservationUserCommentId) {
		Map<String, Object> params = new HashMap<>();
		params.put("comment", comment);
		params.put("reservationUserCommentId", reservationUserCommentId);
		return jdbc.update(UPDATE_COMMENT_OF_RESERVATION_USER_COMMENT, params);
	}
	
}

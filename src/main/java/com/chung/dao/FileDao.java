package com.chung.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.chung.dto.fileinfo.FileInfo;

import static com.chung.dao.ReservationProgramSqls.UPDATE_DELETE_FLAG_OF_FILE_INFO;

@Repository
public class FileDao {
	private NamedParameterJdbcTemplate jdbc;
	private SimpleJdbcInsert insertFileInfoAction;

	public FileDao(DataSource dataSource) {
		insertFileInfoAction = new SimpleJdbcInsert(dataSource).withTableName("file_info")
				.usingGeneratedKeyColumns("id");
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public long insertFileInfo(FileInfo fileInfo) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(fileInfo);
		return insertFileInfoAction.executeAndReturnKey(params).longValue();
	}

	public int updateDeleteFlagOfFileInfo(int deleteFlag, int reservationUserCommentImageId) {

		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("deleteFlag", deleteFlag);
		params.put("reservationUserCommentImageId", reservationUserCommentImageId);
		return jdbc.update(UPDATE_DELETE_FLAG_OF_FILE_INFO, params);
	}
}

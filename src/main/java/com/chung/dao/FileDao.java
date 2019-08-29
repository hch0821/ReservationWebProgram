package com.chung.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.chung.dto.fileinfo.FileInfo;

@Repository
public class FileDao {
	private SimpleJdbcInsert insertFileInfoAction;
	public FileDao(DataSource dataSource) {
		insertFileInfoAction = new SimpleJdbcInsert(dataSource).withTableName("file_info")
				.usingGeneratedKeyColumns("id");
	}
	public long insertFileInfo(FileInfo fileInfo) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(fileInfo);
		return insertFileInfoAction.executeAndReturnKey(params).longValue();
	}
}

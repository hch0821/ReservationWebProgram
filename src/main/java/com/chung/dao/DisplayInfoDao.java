package com.chung.dao;

import static com.chung.dao.ReservationProgramSqls.SELECT_DISPLAY_INFO;
import static com.chung.dao.ReservationProgramSqls.SELECT_DISPLAY_INFO_IMAGE;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;

@Repository
public class DisplayInfoDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<DisplayInfo> displayInfoMapper = BeanPropertyRowMapper.newInstance(DisplayInfo.class);
	private RowMapper<DisplayInfoImage> displayInfoImageMapper = BeanPropertyRowMapper
			.newInstance(DisplayInfoImage.class);
	
	private DisplayInfoDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DisplayInfo selectDisplayInfo(Integer displayInfoId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("displayInfoId", displayInfoId);
		return jdbc.queryForObject(SELECT_DISPLAY_INFO, params, displayInfoMapper);
	}

	public DisplayInfoImage selectDisplayInfoImage(Integer displayInfoId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("displayInfoId", displayInfoId);
		return jdbc.queryForObject(SELECT_DISPLAY_INFO_IMAGE, params, displayInfoImageMapper);
	}
}

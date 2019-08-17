package com.chung.dao;

import static com.chung.dao.ReservSqls.INSERT_RESERVATION_INFO;
import static com.chung.dao.ReservSqls.INSERT_RESERVATION_INFO_PRICE;
import static com.chung.dao.ReservSqls.SELECT_RESERVATION_INFOS_BY_RESERVATION_EMAIL;
import static com.chung.dao.ReservSqls.SELECT_RESERVATION_INFO_BY_RESERVATION_INFO_ID;
import static com.chung.dao.ReservSqls.SELECT_RESERVATION_INFO_PRICES;
import static com.chung.dao.ReservSqls.SELECT_TOTAL_PRICE_OF_RESERVATION;
import static com.chung.dao.ReservSqls.UPDATE_CANCEL_FLAG_OF_RESERVATION_INFO;
import static com.chung.dao.ReservSqls.UPDATE_MODIFY_DATE_RESERVATION_INFO;

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

import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationInfoForDao;
import com.chung.dto.reservation.ReservationPrice;

@Repository
public class ReservationDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<ReservationInfo> reservationInfoMapper = BeanPropertyRowMapper.newInstance(ReservationInfo.class);
	private RowMapper<ReservationPrice> reservationPriceMapper = BeanPropertyRowMapper.newInstance(ReservationPrice.class);
	private SimpleJdbcInsert insertReservationInfoAction;
	private SimpleJdbcInsert insertReservationInfoPriceAction;
	
	private ReservationDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
		insertReservationInfoAction = new SimpleJdbcInsert(dataSource)
				.withTableName("reservation_info")
				.usingGeneratedKeyColumns("id");
		insertReservationInfoPriceAction = new SimpleJdbcInsert(dataSource)
				.withTableName("reservation_info_price")
				.usingGeneratedKeyColumns("id");
	}
	
	public ReservationInfo selectReservationInfo(int reservationInfoId) 
	{
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.queryForObject(SELECT_RESERVATION_INFO_BY_RESERVATION_INFO_ID, params, reservationInfoMapper);
	}
	
	public List<ReservationInfo> selectReservationInfos(String reservationEmail) 
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("reservationEmail", reservationEmail);
		return jdbc.query(SELECT_RESERVATION_INFOS_BY_RESERVATION_EMAIL, params, reservationInfoMapper);
	}

	public int selectTotalPriceofReservation(int reservationInfoId) 
	{
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.queryForObject(SELECT_TOTAL_PRICE_OF_RESERVATION, params, Integer.class);
	}
	
	public int updateCancelFlagOfReservationInfo(int reservationInfoId, int cancelFlag)
	{
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		params.put("cancelFlag", cancelFlag);
		return jdbc.update(UPDATE_CANCEL_FLAG_OF_RESERVATION_INFO, params);
	}
	
	public int updateModifyDateOfReservationInfo(int reservationInfoId)
	{
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.update(UPDATE_MODIFY_DATE_RESERVATION_INFO, params);
	}
	
	public List<ReservationPrice> selectReservationInfoPrices(int reservationInfoId)
	{
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.query(SELECT_RESERVATION_INFO_PRICES, params, reservationPriceMapper);
	}

	public Long insertReservationInfo(ReservationInfoForDao reservationInfoForDao)
	{
		SqlParameterSource params = new BeanPropertySqlParameterSource(reservationInfoForDao);
		return insertReservationInfoAction.executeAndReturnKey(params).longValue();
	}
	
	public Long insertReservationInfoPrice(ReservationPrice reservationPrice)
	{
		SqlParameterSource params = new BeanPropertySqlParameterSource(reservationPrice);
		return insertReservationInfoPriceAction.executeAndReturnKey(params).longValue();
	}
}

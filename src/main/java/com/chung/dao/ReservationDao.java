package com.chung.dao;

import static com.chung.dao.ReservationProgramSqls.SELECT_RESERVATION_INFOS_BY_RESERVATION_EMAIL;
import static com.chung.dao.ReservationProgramSqls.SELECT_RESERVATION_INFO_BY_RESERVATION_INFO_ID;
import static com.chung.dao.ReservationProgramSqls.SELECT_RESERVATION_INFO_PRICES;
import static com.chung.dao.ReservationProgramSqls.SELECT_TOTAL_PRICE_OF_RESERVATION;
import static com.chung.dao.ReservationProgramSqls.UPDATE_CANCEL_FLAG_OF_RESERVATION_INFO;

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
import com.chung.dto.reservation.ReservationInfoForInsertAction;
import com.chung.dto.reservation.ReservationPrice;

@Repository
public class ReservationDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<ReservationInfo> reservationInfoMapper = BeanPropertyRowMapper.newInstance(ReservationInfo.class);
	private RowMapper<ReservationPrice> reservationPriceMapper = BeanPropertyRowMapper
			.newInstance(ReservationPrice.class);
	private SimpleJdbcInsert insertReservationInfoAction;
	private SimpleJdbcInsert insertReservationInfoPriceAction;

	public ReservationDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
		insertReservationInfoAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_info")
				.usingGeneratedKeyColumns("id");
		insertReservationInfoPriceAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_info_price")
				.usingGeneratedKeyColumns("id");
	}

	public ReservationInfo selectReservationInfo(long reservationInfoId) {
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.queryForObject(SELECT_RESERVATION_INFO_BY_RESERVATION_INFO_ID, params, reservationInfoMapper);
	}

	public List<ReservationInfo> selectReservationInfos(String reservationEmail) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("reservationEmail", reservationEmail);
		return jdbc.query(SELECT_RESERVATION_INFOS_BY_RESERVATION_EMAIL, params, reservationInfoMapper);
	}

	public int selectTotalPriceofReservation(long reservationInfoId) {
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.queryForObject(SELECT_TOTAL_PRICE_OF_RESERVATION, params, Integer.class);
	}

	public int updateCancelFlagOfReservationInfo(int reservationInfoId, int cancelFlag) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		params.put("cancelFlag", cancelFlag);
		return jdbc.update(UPDATE_CANCEL_FLAG_OF_RESERVATION_INFO, params);
	}

	public List<ReservationPrice> selectReservationInfoPrices(int reservationInfoId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("reservationInfoId", reservationInfoId);
		return jdbc.query(SELECT_RESERVATION_INFO_PRICES, params, reservationPriceMapper);
	}

	public Long insertReservationInfo(ReservationInfoForInsertAction reservationInfoForInsertAction) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(reservationInfoForInsertAction);
		return insertReservationInfoAction.executeAndReturnKey(params).longValue();
	}

	public Long insertReservationInfoPrice(ReservationPrice reservationPrice) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(reservationPrice);
		return insertReservationInfoPriceAction.executeAndReturnKey(params).longValue();
	}
}

package com.chung.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chung.dao.DisplayInfoDao;
import com.chung.dao.ReservationDao;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationInfoForInsertAction;
import com.chung.dto.reservation.ReservationParam;
import com.chung.dto.reservation.ReservationPrice;
import com.chung.service.IReservationService;

@Service
public class ReservationService implements IReservationService {

	@Autowired
	ReservationDao reservationDao;

	@Autowired
	DisplayInfoDao displayInfoDao;

	@Override
	public List<ReservationInfo> inquireReservations(String reservationEmail) {

		List<ReservationInfo> reservationInfos = reservationDao.selectReservationInfos(reservationEmail);
		for (ReservationInfo reservationInfo : reservationInfos) {
			int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfo.getReservationInfoId());
			DisplayInfo displayInfo = displayInfoDao.selectDisplayInfo(reservationInfo.getDisplayInfoId());
			reservationInfo.setDisplayInfo(displayInfo);
			reservationInfo.setTotalPrice(totalPrice);
		}
		return reservationInfos;
	}

	@Override
	public Map<String, Object> makeReservation(ReservationParam reservationParam) {

		Map<String, Object> map = new HashMap<String, Object>();

		ReservationInfo reservationInfo;

		int displayInfoId = reservationParam.getDisplayInfoId();
		DisplayInfo displayInfo = displayInfoDao.selectDisplayInfo(displayInfoId);

		int productId = reservationParam.getProductId();
		String reservationEmail = reservationParam.getReservationEmail();
		String reservationName = reservationParam.getReservationName();
		String reservationTel = reservationParam.getReservationTelephone();
		String reservationDateStr = reservationParam.getReservationYearMonthDay();
		List<ReservationPrice> reservationPrices = reservationParam.getPrices();

		ReservationInfoForInsertAction reservationInfoForInsertAction = new ReservationInfoForInsertAction(
				displayInfoId, productId, reservationEmail, reservationName, reservationTel, null, 0, new Date(), new Date());

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
			Calendar cal = Calendar.getInstance();
			Date reservationDate = simpleDateFormat.parse(reservationDateStr);
			cal.setTime(reservationDate);
			cal.add(Calendar.DATE, (new Random().nextInt(5) + 1));
			reservationDate = new Date(cal.getTimeInMillis());
			reservationInfoForInsertAction.setReservationDate(reservationDate);
		} catch (ParseException e) {
			e.printStackTrace();
			reservationInfoForInsertAction.setReservationDate(new Date());
		} finally {
			long reservationInfoId = reservationDao.insertReservationInfo(reservationInfoForInsertAction).longValue();
			for (ReservationPrice reservationPrice : reservationPrices) {
				reservationPrice.setReservationInfoId(reservationInfoId);
				reservationDao.insertReservationInfoPrice(reservationPrice);
			}
			int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfoId);
			reservationInfo = reservationDao.selectReservationInfo(reservationInfoId);
			reservationInfo.setDisplayInfo(displayInfo);
			reservationInfo.setTotalPrice(totalPrice);
		}

		map.put("reservationPrices", reservationPrices);
		map.put("reservationInfo", reservationInfo);
		return map;
	}

	@Override
	public Map<String, Object> cancelReservation(int reservationInfoId) {

		Map<String, Object> map = new HashMap<String, Object>();

		reservationDao.updateCancelFlagOfReservationInfo(reservationInfoId, 1);
		reservationDao.updateModifyDateOfReservationInfo(reservationInfoId);
		ReservationInfo reservationInfo = reservationDao.selectReservationInfo(reservationInfoId);

		int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfo.getReservationInfoId());
		DisplayInfo displayInfo = displayInfoDao.selectDisplayInfo(reservationInfo.getDisplayInfoId());

		List<ReservationPrice> reservationPrices = reservationDao.selectReservationInfoPrices(reservationInfoId);

		reservationInfo.setDisplayInfo(displayInfo);
		reservationInfo.setTotalPrice(totalPrice);

		map.put("reservationPrices", reservationPrices);
		map.put("reservationInfo", reservationInfo);

		return map;
	}

}

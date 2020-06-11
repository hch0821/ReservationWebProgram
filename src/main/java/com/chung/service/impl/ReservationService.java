package com.chung.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	// 예약 조회
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

	// 예약 하기
	@Override
	public Map<String, Object> reserveTicket(ReservationParam reservationParam) {
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
				displayInfoId, productId, reservationEmail, reservationName, reservationTel, null, 0, new Date(),
				new Date());

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date reservationDate = simpleDateFormat.parse(reservationDateStr);
			reservationInfoForInsertAction.setReservationDate(reservationDate);

			long reservationInfoId = -1;
			try {
				reservationInfoId = reservationDao.insertReservationInfo(reservationInfoForInsertAction).longValue();
				for (ReservationPrice reservationPrice : reservationPrices) {
					reservationPrice.setReservationInfoId(reservationInfoId);
					reservationDao.insertReservationInfoPrice(reservationPrice);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfoId);
			reservationInfo = reservationDao.selectReservationInfo(reservationInfoId);
			reservationInfo.setDisplayInfo(displayInfo);
			reservationInfo.setTotalPrice(totalPrice);

			return createReservationResponse(reservationPrices, reservationInfo);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	// 예약 취소
	@Override
	public Map<String, Object> cancelReservation(int reservationInfoId) {

		reservationDao.updateCancelFlagOfReservationInfo(reservationInfoId, 1);

		ReservationInfo reservationInfo = reservationDao.selectReservationInfo(reservationInfoId);

		int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfo.getReservationInfoId());
		DisplayInfo displayInfo = displayInfoDao.selectDisplayInfo(reservationInfo.getDisplayInfoId());

		List<ReservationPrice> reservationPrices = reservationDao.selectReservationInfoPrices(reservationInfoId);

		reservationInfo.setDisplayInfo(displayInfo);
		reservationInfo.setTotalPrice(totalPrice);

		return createReservationResponse(reservationPrices, reservationInfo);
	}

	// 예약을 취소하거나 등록하였을 때 클라이언트로 보낼 반응 결과 맵 객체를 만드는 함수
	private Map<String, Object> createReservationResponse(List<ReservationPrice> prices,
			ReservationInfo reservationInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("cancelYn", reservationInfo.isCancelYn());
		map.put("createDate", reservationInfo.getCreateDate());
		map.put("displayInfoId", reservationInfo.getDisplayInfoId());
		map.put("modifyDate", reservationInfo.getModifyDate());
		map.put("prices", prices);
		map.put("productId", reservationInfo.getProductId());
		map.put("reservationDate", reservationInfo.getReservationDate());
		map.put("reservationEmail", reservationInfo.getReservationEmail());
		map.put("reservationInfoId", reservationInfo.getReservationInfoId());
		map.put("reservationName", reservationInfo.getReservationName());
		map.put("reservationTelephone", reservationInfo.getReservationTelephone());
		return map;
	}
}

package com.chung.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.chung.config.ApplicationConfig;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.reservation.ReservationInfo;
import com.chung.dto.reservation.ReservationInfoForInsertAction;
import com.chung.dto.reservation.ReservationPrice;

public class DaoTest {

	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		ReservationDao reservationDao = ac.getBean(ReservationDao.class);
		DisplayInfoDao displayInfoDao = ac.getBean(DisplayInfoDao.class);
		
		int reservationInfoId = 23;
//		ReservationInfo reservationInfo = reservationDao.selectReservationInfo(reservationInfoId);
//		int totalPrice = reservationDao.selectTotalPriceofReservation(reservationInfoId);
//		DisplayInfo displayInfo = displayInfoDao.selectDisplayInfo(reservationInfo.getDisplayInfoId());
//		reservationInfo.setDisplayInfo(displayInfo);
//		reservationInfo.setTotalPrice(totalPrice);
//		print(reservationInfo);
		
		//==============================//
//		List<ReservationInfo> reservationInfos = reservationDao.selectReservationInfos("hch0821@naver.com");
//		
//		for(ReservationInfo info : reservationInfos) {
//			int totalPrice2 = reservationDao.selectTotalPriceofReservation(info.getReservationInfoId());
//			DisplayInfo displayInfo2 = displayInfoDao.selectDisplayInfo(info.getDisplayInfoId());
//			info.setDisplayInfo(displayInfo2);
//			info.setTotalPrice(totalPrice2);
//		}
		
//		print(reservationInfos);
		
		//==============================//
		
//		print(reservationDao.updateCancelFlagOfReservationInfo(reservationInfoId, 0));
//		print(reservationDao.updateModifyDateOfReservationInfo(reservationInfoId));
		
		//==============================//
		
//		List<ReservationPrice> reservationPrices =  reservationDao.selectReservationInfoPrices(reservationInfoId);
//		print(reservationPrices);
//		
//		Date todayDate = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(todayDate);
//        cal.add(Calendar.DATE, (new Random().nextInt(5) + 1));
//        Date reservationDate = new Date(cal.getTimeInMillis());
//		print(reservationDao.insertReservationInfo
//		(
//			new ReservationInfoForInsertAction(3, 3, "hch0821@naver.com", "황충희", "010-5555-5555", reservationDate, 0, new Date())
//		));
//		
       //print(reservationDao.insertReservationInfoPrice(new ReservationPrice(1, 7, 20))); //products/1
//		
//		print(reservationDao.selectReservationInfoPrices(reservationInfoId));
		
		print(reservationDao.selectTotalPriceofReservation(23));
		
		
	}

	public static void print(Object object) {
		System.out.println(object);
	}

}

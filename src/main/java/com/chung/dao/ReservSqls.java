package com.chung.dao;

public class ReservSqls {

	//==============================================================================================
	//메인 페이지
	//==============================================================================================
	
	/*카테고리에 해당하는 상품의 개수*/
	public static final String SELECT_PRODUCT_COUNT = 
	"select count(*) "+
	"from  display_info, product "+
	"where product.id = display_info.product_id "+
	"group by category_id";

	public static final String SELECT_CATEGORY = 
	"select id, name from category"; 

	/*프로모션 내용 쿼리*/
	public static final String SELECT_PROMOTION = 
	"select id, product_id as productId "+
	"from promotion";
	

	/*카테고리에 해당하는 상품 정보*/
	public static final String SELECT_ALL_PRODUCT = 
	"select display_info.id as displayInfoId, place_name as placeName, "+
	"content as productContent, description as productDescription, "+
	"product.id as productId "+
	"from  display_info, product "+
	"where product.id = display_info.product_id "+
	"limit :start, :limit";
	
	public static final String SELECT_PRODUCT = 
	"select display_info.id as displayInfoId, place_name as placeName, "+
	"content as productContent, description as productDescription, "+
	"product.id as productId "+
	"from  display_info, product "+
	"where product.id = display_info.product_id "+
	"and category_id=:categoryId limit :start, :limit";
	
	/*상품 이미지 정보*/
	public static final String SELECT_PRODUCT_IMAGE = 
	"SELECT content_type as contentType, create_date as createDate, delete_flag as deleteFlag,"+
	"file_info.id as fileInfoId, file_name as fileName, modify_date as modifyDate,"+
	"product_id as productId, product_image.id as productImageId, save_file_name as saveFileName, type"+
	" from file_info, product_image"+
	" where file_info.id = product_image.file_id and product_id = :productId and type= :type ";
				
	//==============================================================================================
	// 메인 페이지 끝
	//==============================================================================================
	
	
	//==============================================================================================
	//상세 페이지
	//==============================================================================================
	
	/* required: display_info_id */
	/*comments > Comment dto*/
	public final static String SELECT_COMMENTS = 
	"select comment, c.id as commentId, c.create_date, c.modify_date, c.product_id, reservation_date, "+ 
	"reservation_email, c.reservation_info_id, reservation_name, reservation_tel as reservationTelephone, score "+
	"from reservation_info, reservation_user_comment as c "+
	"where reservation_info.id = c.reservation_info_id and "+
	"display_info_id = :displayInfoId";

	/*commentid 에따라 고객들이 남긴 이미지 목록 쿼리*/
	/*CommentImage*/
	public final static String SELECT_COMMENT_IMAGES = 
	"select content_type, create_date, delete_flag, file_info.id as fileId, "+ 
	"file_name, reservation_user_comment_image.id as image_id, modify_date, reservation_info_id, "+ 
	"reservation_user_comment_id, save_file_name "+
	"from reservation_user_comment_image, file_info "+
	"where reservation_user_comment_image.file_id = file_info.id "+
	"and reservation_user_comment_id = :commentId";


	/*DisplayInfo dto*/
	public final static String SELECT_DISPLAY_INFO = 
	"select category_id, category.name as categoryName, display_info.create_date, "+
	"display_info.id as displayInfoId, email, homepage, display_info.modify_date, opening_hours, "+
	"place_lot, place_name, place_street, content as productContent, description as productDescription, event as productEvent, product.id as productId, tel as telephone "+
	"from category, display_info, product "+ 
	"where category.id = product.category_id and "+
	"product.id = display_info.product_id and "+
	"display_info.id = :displayInfoId";

	/*DisplayInfoImage dto 찾아오시는길*/
	public final static String SELECT_DISPLAY_INFO_IMAGE = 
	"select content_type, create_date, delete_flag, display_info_id, display_info_image.id as displayInfoImageId, file_info.id as fileId, file_name, modify_date, save_file_name "+
	"from file_info, display_info_image "+
	"where file_info.id = display_info_image.file_id and display_info_id = :displayInfoId";

	/*productImages> ProductImage dto 상품 이미지*/
	public final static String SELECT_PRODUCT_IMAGES = 
	"select content_type as contentType, create_date as createDate, delete_flag as deleteFlag, "+
	"file_info.id as fileInfoId, file_name as fileName, modify_date as modifyDate, "+
	"product_id as productId, product_image.id as productImageId, save_file_name as saveFileName, type "+
	"from file_info, product_image "+
	"where file_info.id = product_image.file_id and product_id = :productId and type <> 'th'";
	    
	/*productPrices > ProductPrice dto*/
	public final static String SELECT_PRODUCT_PRICES = 
	"select create_date, discount_rate, modify_date, price, price_type_name, product_id, id "+
	"from product_price " +
	"where product_id = :productId";
	
	//==============================================================================================
	//상세 페이지 끝
	//==============================================================================================
	
	
	//==============================================================================================
	//예약 페이지 시작
	//==============================================================================================
	
	/*========예약 정보 조회========*/
	// required : reservation_email
	public final static String SELECT_RESERVATION_INFOS_BY_RESERVATION_EMAIL = 
	"select cancel_flag as cancelYn, create_date, display_info_id, modify_date, product_id, "+
	"reservation_date, reservation_email, id as reservationInfoId, reservation_name, reservation_tel as reservationTelephone "+
	"from reservation_info where reservation_email= :reservationEmail";

	/*예약한 상품 총 가격*/
	public final static String SELECT_TOTAL_PRICE_OF_RESERVATION = 
	"select sum((price * 0.01 * (100 - discount_rate)) * (select group_concat(count) from reservation_info_price where reservation_info_id = 1)) as totalPrice "+
	"from product_price "+
	"where id in (select product_price_id from reservation_info_price where reservation_info_id = :reservationInfoId)";
	
	/*=========예약하기========*/
	public static final String INSERT_RESERVATION_INFO = 
	"insert into reservation_info (display_info_id, product_id, reservation_email, reservation_name, reservation_tel, reservation_date, create_date, modify_date) "+
	"values (:displayInfoId, :productId, :reservationEmail, :reservationName, :reservationTel, :reservationDate, current_timestamp, current_timestamp)";

	public static final String INSERT_RESERVATION_INFO_PRICE = 
	"insert into reservation_info_price (count, product_price_id, reservation_info_id) "+
	"values (:count, :productPriceId, :reservationInfoId)";
//	insert into reservation_info_price (count, product_price_id, reservation_info_id, id)
//	values (1, 8, 18, 21);
//	insert into reservation_info_price (count, product_price_id, reservation_info_id, id)
//	values (1, 9, 18, 22);

	/*=========예약 취소 ========*/
	public final static String UPDATE_CANCEL_FLAG_OF_RESERVATION_INFO = 
	"update reservation_info set cancel_flag = :cancelFlag where id = :reservationInfoId";
	
	public final static String UPDATE_MODIFY_DATE_RESERVATION_INFO = 
	"update reservation_info set modify_date = current_timestamp where id = :reservationInfoId";

	/*=========예약 걸과/ 예약 취소 결과======*/
	public final static String SELECT_RESERVATION_INFO_BY_RESERVATION_INFO_ID =
	"select cancel_flag as cancelYn, create_date, display_info_id, modify_date, product_id, "+ 
	"reservation_date, reservation_email, id as reservationInfoId, reservation_name, reservation_tel as reservationTelephone "+
	"from reservation_info "+
	"where id = :reservationInfoId"; 
	
	public final static String SELECT_RESERVATION_INFO_PRICES = 
	"select count, product_price_id, reservation_info_id, id as reservationInfoPriceId "+
	"from reservation_info_price "+
	"where reservation_info_id = :reservationInfoId"; 
	
	//==============================================================================================
	//예약 페이지 끝
	//==============================================================================================
}

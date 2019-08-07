package com.chung.dao;

public class DetailSqls {
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
}

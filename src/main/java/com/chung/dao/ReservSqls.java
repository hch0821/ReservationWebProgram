package com.chung.dao;

public class ReservSqls {
	/*=====메인 페이지====*/
	public static final String SELECT_ALL_PRODUCT_COUNT = 
			"select count(*) "+
			"from  display_info, product "+
			"where product.id = display_info.product_id "; /*카테고리에 해당하는 상품의 개수*/
			
	
	public static final String SELECT_PRODUCT_COUNT = 
			SELECT_ALL_PRODUCT_COUNT+ "and category_id=:categoryId";
	
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
			
}

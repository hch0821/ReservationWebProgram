package com.chung.dao;

import static com.chung.dao.ReservSqls.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chung.dto.Category;
import com.chung.dto.Product;
import com.chung.dto.ProductImage;
import com.chung.dto.Promotion;

@Repository
public class ReservDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Category> categoryMapper = BeanPropertyRowMapper.newInstance(Category.class);
	private RowMapper<Product> productMapper = BeanPropertyRowMapper.newInstance(Product.class);
	private RowMapper<Promotion> promotionMapper = BeanPropertyRowMapper.newInstance(Promotion.class);
	private RowMapper<ProductImage> productImageMapper = BeanPropertyRowMapper.newInstance(ProductImage.class);
	private final int NUM_ITEM = 4;
	private ReservDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Product> selectAllProduct(Integer start){
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("start", start);
		params.put("limit",NUM_ITEM);
		return jdbc.query(SELECT_ALL_PRODUCT,params,productMapper);
	}
	
	public List<Product> selectProduct(Integer categoryId, Integer start){
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("categoryId", categoryId);
		params.put("start", start);
		params.put("limit",NUM_ITEM);
		
		return jdbc.query(SELECT_PRODUCT,params,productMapper);
	}
	
	public int selectAllProductCount() {
		return jdbc.queryForObject(SELECT_ALL_PRODUCT_COUNT, Collections.emptyMap(), Integer.class);
	}
	public int selectProductCount(Integer categoryId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("categoryId", categoryId);
		return jdbc.queryForObject(SELECT_PRODUCT_COUNT, params, Integer.class);
	}
	
	public List<Category> selectCategory(){
		return jdbc.query(SELECT_CATEGORY, categoryMapper);
	}
	
	public List<Promotion> selectPromotion(){
		return jdbc.query(SELECT_PROMOTION, promotionMapper);
	}
	
	public List<ProductImage> selectProductImage(Integer productId, String type){
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("type", type);
		return jdbc.query(SELECT_PRODUCT_IMAGE, params, productImageMapper);
	}
}

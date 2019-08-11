package com.chung.dao;

import static com.chung.dao.ReservSqls.SELECT_ALL_PRODUCT;
import static com.chung.dao.ReservSqls.SELECT_CATEGORY;
import static com.chung.dao.ReservSqls.SELECT_COMMENTS;
import static com.chung.dao.ReservSqls.SELECT_COMMENT_IMAGES;
import static com.chung.dao.ReservSqls.SELECT_DISPLAY_INFO;
import static com.chung.dao.ReservSqls.SELECT_DISPLAY_INFO_IMAGE;
import static com.chung.dao.ReservSqls.SELECT_PRODUCT;
import static com.chung.dao.ReservSqls.SELECT_PRODUCT_COUNT;
import static com.chung.dao.ReservSqls.SELECT_PRODUCT_IMAGE;
import static com.chung.dao.ReservSqls.SELECT_PRODUCT_IMAGES;
import static com.chung.dao.ReservSqls.SELECT_PRODUCT_PRICES;
import static com.chung.dao.ReservSqls.SELECT_PROMOTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.chung.dto.category.Category;
import com.chung.dto.comment.Comment;
import com.chung.dto.comment.CommentImage;
import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;
import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.dto.promotion.Promotion;
import com.chung.service.IProductService;

@Repository
public class ReservDao {

	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Category> categoryMapper = BeanPropertyRowMapper.newInstance(Category.class);
	private RowMapper<Product> productMapper = BeanPropertyRowMapper.newInstance(Product.class);
	private RowMapper<Promotion> promotionMapper = BeanPropertyRowMapper.newInstance(Promotion.class);
	private RowMapper<ProductImage> productImageMapper = BeanPropertyRowMapper.newInstance(ProductImage.class);

	private RowMapper<Comment> commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);
	private RowMapper<CommentImage> commentImageMapper = BeanPropertyRowMapper.newInstance(CommentImage.class);
	private RowMapper<DisplayInfo> displayInfoMapper = BeanPropertyRowMapper.newInstance(DisplayInfo.class);
	private RowMapper<DisplayInfoImage> displayInfoImageMapper = BeanPropertyRowMapper
			.newInstance(DisplayInfoImage.class);
	private RowMapper<ProductPrice> productPriceMapper = BeanPropertyRowMapper.newInstance(ProductPrice.class);

	private ReservDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	// ==============================================================================================
	// 메인 페이지
	// ==============================================================================================

	public List<Product> selectAllProduct(Integer start) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("start", start);
		params.put("limit", IProductService.NUM_ITEM);
		return jdbc.query(SELECT_ALL_PRODUCT, params, productMapper);
	}

	public List<Product> selectProducts(Integer categoryId, Integer start) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("categoryId", categoryId);
		params.put("start", start);
		params.put("limit", IProductService.NUM_ITEM);

		return jdbc.query(SELECT_PRODUCT, params, productMapper);
	}

	public List<Integer> selectProductCount() {
		SqlRowSet rowset = jdbc.queryForRowSet(SELECT_PRODUCT_COUNT, Collections.emptyMap());
		List<Integer> counts = new ArrayList<Integer>();
		while (rowset.next()) {
			counts.add(rowset.getInt("count(*)"));
		}
		return counts;
	}

	public List<Category> selectCategory() {
		return jdbc.query(SELECT_CATEGORY, categoryMapper);
	}

	public List<Promotion> selectPromotion() {
		return jdbc.query(SELECT_PROMOTION, promotionMapper);
	}

	public ProductImage selectProductImage(Integer productId, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("type", type);
		return jdbc.queryForObject(SELECT_PRODUCT_IMAGE, params, productImageMapper);
	}
	// ==============================================================================================
	// 메인 페이지 끝
	// ==============================================================================================

	// ==============================================================================================
	// 상세 페이지
	// ==============================================================================================
	public List<Comment> selectComments(Integer displayInfoId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("displayInfoId", displayInfoId);
		return jdbc.query(SELECT_COMMENTS, params, commentMapper);
	}

	public List<CommentImage> selectCommentImages(Integer commentId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("commentId", commentId);

		return jdbc.query(SELECT_COMMENT_IMAGES, params, commentImageMapper);
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

	public List<ProductImage> selectProductImages(Integer productId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("productId", productId);
		return jdbc.query(SELECT_PRODUCT_IMAGES, params, productImageMapper);
	}

	public List<ProductPrice> selectProducPrices(Integer productId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("productId", productId);
		return jdbc.query(SELECT_PRODUCT_PRICES, params, productPriceMapper);
	}
	// ==============================================================================================
	// 상세 페이지 끝
	// ==============================================================================================

}

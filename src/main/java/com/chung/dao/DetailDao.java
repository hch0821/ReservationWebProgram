package com.chung.dao;

import static com.chung.dao.DetailSqls.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chung.dto.Comment;
import com.chung.dto.CommentImage;
import com.chung.dto.DisplayInfo;
import com.chung.dto.DisplayInfoImage;
import com.chung.dto.ProductImage;
import com.chung.dto.ProductPrice;

@Repository
public class DetailDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Comment> commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);
	private RowMapper<CommentImage> commentImageMapper = BeanPropertyRowMapper.newInstance(CommentImage.class);
	private RowMapper<DisplayInfo> displayInfoMapper = BeanPropertyRowMapper.newInstance(DisplayInfo.class);
	private RowMapper<DisplayInfoImage> displayInfoImageMapper = BeanPropertyRowMapper
			.newInstance(DisplayInfoImage.class);
	private RowMapper<ProductImage> productImageMapper = BeanPropertyRowMapper.newInstance(ProductImage.class);
	private RowMapper<ProductPrice> productPriceMapper = BeanPropertyRowMapper.newInstance(ProductPrice.class);

	private DetailDao(DataSource datasource) {
		jdbc = new NamedParameterJdbcTemplate(datasource);

	}

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

}

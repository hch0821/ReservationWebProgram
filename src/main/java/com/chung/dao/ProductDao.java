package com.chung.dao;

import static com.chung.dao.ReservationProgramSqls.*;

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

import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;
import com.chung.service.IProductService;

@Repository
public class ProductDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Product> productMapper = BeanPropertyRowMapper.newInstance(Product.class);
	private RowMapper<ProductPrice> productPriceMapper = BeanPropertyRowMapper.newInstance(ProductPrice.class);
	private RowMapper<ProductImage> productImageMapper = BeanPropertyRowMapper.newInstance(ProductImage.class);

	public ProductDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public List<Product> selectProducts(Integer categoryId, Integer start) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("categoryId", categoryId);
		params.put("start", start);
		params.put("limit", IProductService.NUM_ITEM);

		return jdbc.query(SELECT_PRODUCT, params, productMapper);
	}

	public List<Product> selectAllProduct(Integer start) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("start", start);
		params.put("limit", IProductService.NUM_ITEM);
		return jdbc.query(SELECT_ALL_PRODUCT, params, productMapper);
	}

	public List<Integer> selectProductCount() {
		SqlRowSet rowset = jdbc.queryForRowSet(SELECT_PRODUCT_COUNT, Collections.emptyMap());
		List<Integer> counts = new ArrayList<Integer>();
		while (rowset.next()) {
			counts.add(rowset.getInt("count(*)"));
		}
		return counts;
	}

	public ProductImage selectProductImage(Integer productId, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("type", type);
		return jdbc.queryForObject(SELECT_PRODUCT_IMAGE, params, productImageMapper);
	}

	public List<ProductImage> selectProductImages(Integer productId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("productId", productId);
		return jdbc.query(SELECT_PRODUCT_IMAGES, params, productImageMapper);
	}

	public List<ProductPrice> selectProductPrices(Integer productId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("productId", productId);
		return jdbc.query(SELECT_PRODUCT_PRICES, params, productPriceMapper);
	}
}

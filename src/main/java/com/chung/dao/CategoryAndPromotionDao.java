package com.chung.dao;

import static com.chung.dao.ReservSqls.SELECT_CATEGORY;
import static com.chung.dao.ReservSqls.SELECT_PROMOTION;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chung.dto.category.Category;
import com.chung.dto.promotion.Promotion;

@Repository
public class CategoryAndPromotionDao {
	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Category> categoryMapper = BeanPropertyRowMapper.newInstance(Category.class);
	private RowMapper<Promotion> promotionMapper = BeanPropertyRowMapper.newInstance(Promotion.class);
	private CategoryAndPromotionDao(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
	public List<Category> selectCategory() {
		return jdbc.query(SELECT_CATEGORY, categoryMapper);
	}

	public List<Promotion> selectPromotion() {
		return jdbc.query(SELECT_PROMOTION, promotionMapper);
	}
}

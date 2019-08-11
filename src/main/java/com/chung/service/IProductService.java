package com.chung.service;

import java.util.List;

import com.chung.dto.product.Product;
import com.chung.dto.product.ProductImage;
import com.chung.dto.product.ProductPrice;

public interface IProductService {
	public static final int NUM_ITEM = 4;

	public List<Product> getProducts(int categoryId, int start);

	public List<ProductPrice> getProductPrices(int productId);

	public ProductImage getProductImage(int productId, String type);

	public List<ProductImage> getProductImages(int productId);

}

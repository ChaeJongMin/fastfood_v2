package com.example.demo.persistence;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Categories;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductImage;

@EnableJpaRepositories
public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
//	public List<ProductImage> findByImageLoad(String searchword);
	
	public List<ProductImage> findByProduct(Product searchword);
    
}

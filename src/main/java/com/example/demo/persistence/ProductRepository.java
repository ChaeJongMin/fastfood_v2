package com.example.demo.persistence;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;


public interface ProductRepository extends CrudRepository<Product,Integer> {
	public List<Product> findByProductName(String searchword);

	@Transactional
	@Modifying
	@Query("delete from Product b where b.pId=:pid")  //:id는 함수의 매개변수
	void deleteProduct(Integer pid);
	
}

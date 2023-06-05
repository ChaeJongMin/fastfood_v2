package com.example.demo.persistence;
import java.util.List;

import javax.print.attribute.standard.PresentationDirection;
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
	@Query("delete from Product b where b.pid=:pid")  //:id는 함수의 매개변수
	void deleteProduct(Integer pid);

	List<Product> findByCategories_CategoryId(int id);
	List<Product> findByCategories_CategoryIdOrCategories_CategoryId(int id1, int id2);

	@Query("select p from Product p inner join Categories c on p.categories.categoryId=c.categoryId where c.categoryName=:cname")
	List<Product> findByCategoryName(String cname);
}

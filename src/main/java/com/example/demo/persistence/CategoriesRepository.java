package com.example.demo.persistence;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;

public interface CategoriesRepository extends CrudRepository<Categories, Integer>{
	public List<Categories> findByCategoryName(String searchword);

	
	@Query("select b from Categories b where b.categoryName=:name1 or b.categoryName=:name2")
	public List<Categories> findbyDrinkMenu(String name1, String name2);
}

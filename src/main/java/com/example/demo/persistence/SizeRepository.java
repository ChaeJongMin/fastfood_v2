package com.example.demo.persistence;
import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;
import com.example.demo.domain.Size;

public interface SizeRepository extends CrudRepository<Size,Integer> {
	public List<Size> findBySizename(String searchword);

}



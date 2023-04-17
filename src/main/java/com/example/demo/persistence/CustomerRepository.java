package com.example.demo.persistence;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	public List<Customer> findByUserId(String searchword);
	public List<Customer> findByRole(int searchword);
	public boolean existsByUserId(String id);
	
}

	package com.example.demo.persistence;
	import java.util.List;
	
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.CrudRepository;
	import org.springframework.data.repository.query.Param;
	
	import com.example.demo.domain.Basket;
	import com.example.demo.domain.Customer;
	public interface BasketRepository extends CrudRepository<Basket, Integer> {
		public List<Basket> findByCustomer(Customer searchword);
			
	}

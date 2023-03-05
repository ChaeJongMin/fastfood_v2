	package com.example.demo.persistence;
	import java.util.List;
	import java.util.Optional;
	import org.springframework.data.jpa.repository.JpaRepository;

	import com.example.demo.domain.Basket;
	import com.example.demo.domain.Customer;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;

	import org.springframework.transaction.annotation.Transactional;

	public interface BasketRepository extends JpaRepository<Basket, Integer> {
		public List<Basket> findByCustomer(Customer searchword);
		public List<Basket> findByCustomer_Id(int userId);
		public Optional<Basket> findByCustomer_IdAndProductinfo_InfoidAndInfo(int userId, int optionId,String infoStr);
		@Transactional(readOnly = true)
		@Query("SELECT COALESCE(SUM(b.pCount),0) FROM Basket b WHERE b.customer.id=:userId")
		Integer countByCustomerId(Integer userId);
	}

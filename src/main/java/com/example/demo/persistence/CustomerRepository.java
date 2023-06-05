package com.example.demo.persistence;
import java.util.List;
import java.util.Optional;

import com.example.demo.domain.SocialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.example.demo.domain.Chat;
import com.example.demo.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	public List<Customer> findByUserId(String searchword);
	public Optional<Customer> findByEmail(String searchword);
	public List<Customer> findByRole(int searchword);
	public boolean existsByUserId(String id);
	public Optional<Customer> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

	public List<Customer> findByUserIdContainingIgnoreCase(String search);

	///chat
	@Query(value = "select ch.chat_id from Customer c inner join Chat as ch on c.id = ch.receiver_id or c.id = ch.sender_id where (ch.sender_id =:myId or ch.sender_id =:opponentId) and (ch.receiver_id =:myId or ch.receiver_id =:opponentId) limit 1" , nativeQuery = true)
	Optional <Integer> getChatCount(@Param("myId") int myId, @Param("opponentId") int opponentId);


}

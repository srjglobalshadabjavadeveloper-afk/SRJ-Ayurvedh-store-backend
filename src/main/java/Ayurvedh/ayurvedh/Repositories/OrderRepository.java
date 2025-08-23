package Ayurvedh.ayurvedh.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Ayurvedh.ayurvedh.entity.Order;
import Ayurvedh.ayurvedh.entity.Users;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("select o from Order o where o.user = :user")
	List<Order> findByUser(@Param("user") Users user);

	@Query("select o from Order o where o.user.id = :userId")
	List<Order> findByUserId(@Param("userId") Long userId);

	@Query("select o from Order o where o.order_id = :orderId")
	Order findByOrderId(@Param("orderId") String orderId);
}



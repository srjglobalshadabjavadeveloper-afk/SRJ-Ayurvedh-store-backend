package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Ayurvedh.ayurvedh.entity.OrderDetails;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    
    // Find order details by order ID
    List<OrderDetails> findByOrderId(Long orderId);
    
    // Find order details by product ID
    List<OrderDetails> findByProductId(Long productId);
    
    // Find order details by order and product
    List<OrderDetails> findByOrderIdAndProductId(Long orderId, Long productId);
}

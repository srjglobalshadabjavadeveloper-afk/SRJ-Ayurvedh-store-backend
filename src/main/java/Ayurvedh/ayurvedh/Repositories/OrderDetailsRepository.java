package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Ayurvedh.ayurvedh.entity.OrderDetails;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    
    List<OrderDetails> findByOrderId(Long orderId);
    
    List<OrderDetails> findByProductId(Long productId);
    
    List<OrderDetails> findByOrderIdAndProductId(Long orderId, Long productId);
}

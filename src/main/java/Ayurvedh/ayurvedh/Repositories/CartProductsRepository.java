package Ayurvedh.ayurvedh.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Ayurvedh.ayurvedh.entity.CartProducts;

public interface CartProductsRepository extends JpaRepository<CartProducts, Long> {
    Optional<CartProducts> findByUserIdAndProductId(Long userId, Long productId);
    List<CartProducts> findByUserId(Long userId);
    Optional<CartProducts> findByIdAndUserId(Long id, Long userId);
    void deleteByUserId(Long userId);
}



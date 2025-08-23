package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.Category;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByCategory(Category category);
    List<Products> findByCategoryId(Long categoryId);
}

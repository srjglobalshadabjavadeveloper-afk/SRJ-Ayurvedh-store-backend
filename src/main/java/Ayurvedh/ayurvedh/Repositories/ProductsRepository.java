package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.Category;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByCategory(Category category);
    List<Products> findByCategoryId(Long id);
    List<Products> findBySubCategoryId(Long subCategoryId);


      // Add a custom query method
    @Query("SELECT p FROM Products p WHERE p.category.id = :categoryId")
    List<Products> findProductsByCategoryId(@Param("categoryId") Long categoryId);
    
    // Add a count method
    @Query("SELECT COUNT(p) FROM Products p WHERE p.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
}

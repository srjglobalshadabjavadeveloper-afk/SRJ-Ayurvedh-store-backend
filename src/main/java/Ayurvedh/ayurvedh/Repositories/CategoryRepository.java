package Ayurvedh.ayurvedh.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Ayurvedh.ayurvedh.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
      // List<Category> findByCategoryId(Long id);
}

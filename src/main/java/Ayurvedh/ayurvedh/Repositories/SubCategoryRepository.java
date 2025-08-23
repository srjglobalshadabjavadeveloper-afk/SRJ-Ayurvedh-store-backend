package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ayurvedh.ayurvedh.entity.SubCategory;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findByNameAndCategory_Id(String name, Long categoryId);
    List<SubCategory> findByCategory_Id(Long categoryId);
}

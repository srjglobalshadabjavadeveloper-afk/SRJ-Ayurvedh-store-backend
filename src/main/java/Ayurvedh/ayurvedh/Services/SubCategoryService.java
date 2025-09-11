package Ayurvedh.ayurvedh.Services;

import Ayurvedh.ayurvedh.dto.AddSubCategoryDto;
import Ayurvedh.ayurvedh.entity.SubCategory;

import java.util.List;

public interface SubCategoryService {
    SubCategory addSubCategory(AddSubCategoryDto dto);
    List<SubCategory> getSubCategoriesByCategory(Long categoryId);
    List<SubCategory> getAllSubCategories();
    SubCategory getSubCategoryById(Long id);
    SubCategory updateSubCategory(Long id, AddSubCategoryDto dto);
    void deleteSubCategory(Long id);

     int countSubCategoriesByCategory(Long categoryId);
}

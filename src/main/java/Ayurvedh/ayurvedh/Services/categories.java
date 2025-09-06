package Ayurvedh.ayurvedh.Services;

import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import java.util.List;

public interface categories {
    
    Category addCategory(AddCategoryDto addCategoryDto);
    
    // List<Category> getAllCategories();
    
     List<AddCategoryDto> getAllCategories();
    Category getCategoryById(Long id);
    
    Category updateCategory(Long id, AddCategoryDto addCategoryDto);
    
    void deleteCategory(Long id);
}

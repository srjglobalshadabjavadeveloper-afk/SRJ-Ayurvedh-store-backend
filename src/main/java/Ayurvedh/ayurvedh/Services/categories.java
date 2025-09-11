package Ayurvedh.ayurvedh.Services;

import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.dto.AddSubCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.SubCategory;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface categories {

    Category addCategory(AddCategoryDto addCategoryDto);

    List<AddCategoryDto> getAllCategories();

    Category getCategoryById(Long id);

    Category updateCategory(Long id, String name, MultipartFile image);

    void deleteCategory(Long id);

}

package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Services.CloudinaryImageService;
import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import jakarta.transaction.Transactional;
import Ayurvedh.ayurvedh.Repositories.CategoryRepository;
import Ayurvedh.ayurvedh.Repositories.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements categories {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @Override
    public Category addCategory(AddCategoryDto addCategoryDto) {
        // Check if category with same name already exists
        Category existingCategory = categoryRepository.findByName(addCategoryDto.getName());
        if (existingCategory != null) {
            throw new RuntimeException("Category with name '" + addCategoryDto.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(addCategoryDto.getName());
        category.setImage(addCategoryDto.getImage());
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        return categoryRepository.save(category);
    }

    @Override
    public List<AddCategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AddCategoryDto convertToDTO(Category category) {
        AddCategoryDto dto = new AddCategoryDto();
        dto.setId(category.getId()); 
        dto.setName(category.getName());
        dto.setImage(category.getImage());
        return dto;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, String name, MultipartFile image) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        Category existingCategory = categoryRepository.findByName(name);
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new RuntimeException("Category with name '" + name + "' already exists");
        }
        category.setName(name);

        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinaryImageService.uploadImage(image);
                String imageUrl = (String) uploadResult.get("secure_url"); 
                category.setImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image to Cloudinary: " + e.getMessage());
            }
        }

        category.setUpdatedAt(new Date());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
    Category category = categoryRepository.findById(id).orElse(null);
    if (category == null) {
    throw new RuntimeException("Category not found with id: " + id);
    }
    categoryRepository.delete(category);
    }

    // @Override
    // @Transactional
    // public void deleteCategory(Long id) {

    //     try {
    //         Category category = categoryRepository.findById(id).orElse(null);
    //         if (category == null) {
    //             throw new RuntimeException("Category not found with id: " + id);
    //         }

    //         if (!category.getSubCategories().isEmpty()) {
    //             throw new RuntimeException("Cannot delete category with existing subcategories");
    //         }

    //         List<Products> products = productsRepository.findProductsByCategoryId(id);
    //         if (!products.isEmpty()) {
    //             throw new RuntimeException("Cannot delete category with existing products");
    //         }

    //         categoryRepository.delete(category);
    //     } catch (Exception e) {
    //         System.out.println("Error in deleteCategory: " + e.getMessage());
    //         throw e;
    //     }
    // }

    // @Override
    // public List<Category> getCategoryByProduct(Long id) {
    //     return categoryRepository.findByCategoryId(id);
    // }

//  @Override
//     public List<AddSubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
//         Category category = categoryRepository.findById(categoryId)
//             .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
//         return category.getSubCategories().stream()
//             .map(this::convertToDto)
//             .collect(Collectors.toList());
//     }
    
//     private AddSubCategoryDto convertToDto(AddSubCategoryDto subCategory) {
//         AddSubCategoryDto dto = new AddSubCategoryDto();
//         dto.setId(subCategory.getId());
//         dto.setName(subCategory.getName());
//         dto.setImage(subCategory.getImage());
//         dto.setCategoryId(subCategory.getCategoryId());
//         dto.setCategoryName(subCategory.getCategoryName());
//         return dto;
//     }

}

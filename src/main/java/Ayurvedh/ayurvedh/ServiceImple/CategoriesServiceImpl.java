package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.Products;
import jakarta.transaction.Transactional;
import Ayurvedh.ayurvedh.Repositories.CategoryRepository;
import Ayurvedh.ayurvedh.Repositories.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements categories {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    // @Override
    // public List<Category> getAllCategories() {
    // return categoryRepository.findAll();
    // }
    @Override
    public List<AddCategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AddCategoryDto convertToDTO(Category category) {
        AddCategoryDto dto = new AddCategoryDto();
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
    public Category updateCategory(Long id, AddCategoryDto addCategoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        // Check if another category with the same name exists (excluding current
        // category)
        Category existingCategory = categoryRepository.findByName(addCategoryDto.getName());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new RuntimeException("Category with name '" + addCategoryDto.getName() + "' already exists");
        }

        category.setName(addCategoryDto.getName());
        category.setImage(addCategoryDto.getImage());
        category.setUpdatedAt(new Date());

        return categoryRepository.save(category);
    }

    // @Override
    // public void deleteCategory(Long id) {
    // Category category = categoryRepository.findById(id).orElse(null);
    // if (category == null) {
    // throw new RuntimeException("Category not found with id: " + id);
    // }
    // categoryRepository.delete(category);
    // }

    @Override
    @Transactional
    public void deleteCategory(Long id) {

        try {
            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                throw new RuntimeException("Category not found with id: " + id);
            }

            // Check if category has subcategories
            if (!category.getSubCategories().isEmpty()) {
                throw new RuntimeException("Cannot delete category with existing subcategories");
            }

            // Check if category has products using the custom query method
            List<Products> products = productsRepository.findProductsByCategoryId(id);
            if (!products.isEmpty()) {
                throw new RuntimeException("Cannot delete category with existing products");
            }

            // If all checks pass, delete the category
            categoryRepository.delete(category);
        } catch (Exception e) {
            System.out.println("Error in deleteCategory: " + e.getMessage());
            throw e;
        }
    }

}

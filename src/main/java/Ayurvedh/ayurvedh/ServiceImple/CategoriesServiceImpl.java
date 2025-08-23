package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoriesServiceImpl implements categories {

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

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category updateCategory(Long id, AddCategoryDto addCategoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        // Check if another category with the same name exists (excluding current category)
        Category existingCategory = categoryRepository.findByName(addCategoryDto.getName());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new RuntimeException("Category with name '" + addCategoryDto.getName() + "' already exists");
        }

        category.setName(addCategoryDto.getName());
        category.setImage(addCategoryDto.getImage());
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
}

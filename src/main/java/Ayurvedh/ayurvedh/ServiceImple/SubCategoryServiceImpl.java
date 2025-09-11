package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Repositories.CategoryRepository;
import Ayurvedh.ayurvedh.Repositories.SubCategoryRepository;
import Ayurvedh.ayurvedh.Services.SubCategoryService;
import Ayurvedh.ayurvedh.dto.AddSubCategoryDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SubCategory addSubCategory(AddSubCategoryDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + dto.getCategoryId());
        }
        SubCategory exists = subCategoryRepository.findByNameAndCategory_Id(dto.getName(), dto.getCategoryId());
        if (exists != null) {
            throw new RuntimeException("SubCategory with name '" + dto.getName() + "' already exists in this category");
        }
        SubCategory sc = new SubCategory();
        sc.setName(dto.getName());
        sc.setImage(dto.getImage());
        sc.setCategory(category);
        sc.setCreatedAt(new Date());
        sc.setUpdatedAt(new Date());
        return subCategoryRepository.save(sc);
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public List<SubCategory> getSubCategoriesByCategory(Long categoryId) {
        return subCategoryRepository.findByCategory_Id(categoryId);
    }
    
     @Override
    public int countSubCategoriesByCategory(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategory_Id(categoryId);
        return subCategories.size();
    }


    @Override
    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public SubCategory updateSubCategory(Long id, AddSubCategoryDto dto) {
        SubCategory sc = subCategoryRepository.findById(id).orElse(null);
        if (sc == null) {
            throw new RuntimeException("SubCategory not found with id: " + id);
        }
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + dto.getCategoryId());
        }
        SubCategory exists = subCategoryRepository.findByNameAndCategory_Id(dto.getName(), dto.getCategoryId());
        if (exists != null && !exists.getId().equals(id)) {
            throw new RuntimeException("SubCategory with name '" + dto.getName() + "' already exists in this category");
        }
        sc.setName(dto.getName());
        sc.setImage(dto.getImage());
        sc.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        sc.setUpdatedAt(new Date());
        return subCategoryRepository.save(sc);
    }


    @Override
    public void deleteSubCategory(Long id) {
        SubCategory sc = subCategoryRepository.findById(id).orElse(null);
        if (sc == null) {
            throw new RuntimeException("SubCategory not found with id: " + id);
        }
        subCategoryRepository.delete(sc);
    }
}

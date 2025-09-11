package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Services.ProductsService;
import Ayurvedh.ayurvedh.dto.AddProductDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.SubCategory;
import Ayurvedh.ayurvedh.Repositories.ProductsRepository;
import Ayurvedh.ayurvedh.Repositories.SubCategoryRepository;
import Ayurvedh.ayurvedh.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public Products addProduct(AddProductDto addProductDto) {
        Products product = new Products();
        product.setName(addProductDto.getName());
        product.setImage(addProductDto.getImage());

        Category category = categoryRepository.findById(addProductDto.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with id: " + addProductDto.getCategoryId()));
        product.setCategory(category);
        SubCategory subCategory = null;
        if (addProductDto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(addProductDto.getSubCategoryId())
                    .orElseThrow(() -> new RuntimeException(
                            "SubCategory not found with id: " + addProductDto.getSubCategoryId()));
        } else {
            List<SubCategory> subCategories = category.getSubCategories();
            if (!subCategories.isEmpty()) {
                subCategory = subCategories.get(1); // Default first subcategory assign
            }
        }
        product.setSubCategory(subCategory);

        product.setUnit(addProductDto.getUnit());
        product.setStock(addProductDto.getStock());
        product.setPrice(addProductDto.getPrice());
        product.setDiscount(addProductDto.getDiscount());
        product.setDescription(addProductDto.getDescription());
        product.setMore_details(addProductDto.getMoreDetails());
        product.setPublish(addProductDto.isPublish());

        Date now = new Date();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return productsRepository.save(product);
    }

    @Override
    public List<Products> getCategoryByProducts(Long categoryId) {
        return productsRepository.findByCategoryId(categoryId);
    }

    @Override
    public Products getProductById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public Products updateProduct(Long id, AddProductDto addProductDto) {
        Products existingProduct = productsRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(addProductDto.getName());
        existingProduct.setImage(addProductDto.getImage());

        // Update category
        Category category = categoryRepository.findById(addProductDto.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with id: " + addProductDto.getCategoryId()));
        existingProduct.setCategory(category);

        // Update subcategory if provided
        if (addProductDto.getSubCategoryId() != null) {
            SubCategory subCategory = new SubCategory();
            subCategory.setId(addProductDto.getSubCategoryId());
            existingProduct.setSubCategory(subCategory);
        }

        existingProduct.setUnit(addProductDto.getUnit());
        existingProduct.setStock(addProductDto.getStock());
        existingProduct.setPrice(addProductDto.getPrice());
        existingProduct.setDiscount(addProductDto.getDiscount());
        existingProduct.setDescription(addProductDto.getDescription());
        existingProduct.setMore_details(addProductDto.getMoreDetails());
        existingProduct.setPublish(addProductDto.isPublish());

        existingProduct.setUpdatedAt(new Date());

        return productsRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }

    @Override
    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    // Add this missing method
    @Override
    public long getProductCount() {
        return productsRepository.count();
    }

    //product by sub category
    @Override
    public List<Products> getProductsBySubCategory(Long subCategoryId) {
        return productsRepository.findBySubCategoryId(subCategoryId);
    }

}
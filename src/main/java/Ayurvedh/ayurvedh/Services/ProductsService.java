package Ayurvedh.ayurvedh.Services;

import Ayurvedh.ayurvedh.dto.AddProductDto;
import Ayurvedh.ayurvedh.entity.Products;
import java.util.List;

public interface ProductsService {
    Products addProduct(AddProductDto addProductDto);
    // ProductResponseDto addProduct(AddProductDto addProductDto);
    List<Products> getProductsByCategory(Long categoryId);
    Products getProductById(Long id);
    Products updateProduct(Long id, AddProductDto addProductDto);
    void deleteProduct(Long id);
    List<Products> getAllProducts();

    // Add this method for getting product count
    long getProductCount();
}
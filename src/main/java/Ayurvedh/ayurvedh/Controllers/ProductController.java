package Ayurvedh.ayurvedh.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import Ayurvedh.ayurvedh.Services.ProductsService;
import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.Services.SubCategoryService;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.SubCategory;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:5173")

public class ProductController {

	@Autowired
	private ProductsService productsService;

	@Autowired
	private categories categoriesService;

	@Autowired
	private SubCategoryService subCategoryService;

	@GetMapping("/products/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		try {
			Products product = productsService.getProductById(id);
			if (product == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching product: " + e.getMessage());
		}
	}

	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts() {
		try {
			List<Products> products = productsService.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching products: " + e.getMessage());
		}
	}

    @GetMapping("/subcategories/{id}/products")
    public ResponseEntity<List<Products>> getProductsBySubCategory(@PathVariable Long id) {
        try {
            List<Products> products = productsService.getProductsBySubCategory(id);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

	@GetMapping("/subcategories/category/{categoryId}")
	public ResponseEntity<?> getSubCategoriesByCategory(@PathVariable Long categoryId) {
		try {
			List<SubCategory> list = subCategoryService.getSubCategoriesByCategory(categoryId);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategories: " + e.getMessage());
		}
	}

	@GetMapping("/subcategories/{id}")
	public ResponseEntity<?> getSubCategoryById(@PathVariable Long id) {
		try {
			SubCategory sc = subCategoryService.getSubCategoryById(id);
			if (sc == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(sc);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategory: " + e.getMessage());

		}
	}

	@GetMapping("/subcategories")
	public ResponseEntity<?> getAllSubCategories(Authentication authentication) {
		try {
			List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
			return ResponseEntity.ok(subCategories);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategories: " + e.getMessage());
		}
	}

	@GetMapping("/categories")
	public ResponseEntity<?> getAllCategories() {

		try {
			List<AddCategoryDto> categories = categoriesService.getAllCategories();
			return ResponseEntity.ok(categories);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching categories: " + e.getMessage());
		}
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoriesService.getCategoryById(id);
			if (category == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching category: " + e.getMessage());
		}
	}

	@GetMapping("/categories/{categoryId}/products")
	public ResponseEntity<List<Products>> getProductsByCategory(@PathVariable Long categoryId) {
		List<Products> products = productsService.getCategoryByProducts(categoryId);
		return ResponseEntity.ok(products);
	}

}

// package Ayurvedh.ayurvedh.Controllers;

// import Ayurvedh.ayurvedh.Services.ExcelService;
// import Ayurvedh.ayurvedh.Services.ProductsService;
// import Ayurvedh.ayurvedh.Services.categories;
// import Ayurvedh.ayurvedh.Services.SubCategoryService;
// import Ayurvedh.ayurvedh.dto.AddProductDto;
// import Ayurvedh.ayurvedh.dto.AddCategoryDto;
// import Ayurvedh.ayurvedh.dto.AddSubCategoryDto;
// import Ayurvedh.ayurvedh.dto.BulkUploadResponseDto;
// import Ayurvedh.ayurvedh.entity.Products;
// import Ayurvedh.ayurvedh.entity.Category;
// import Ayurvedh.ayurvedh.entity.SubCategory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @RestController
// @RequestMapping("/admin")
// @CrossOrigin(origins = "http://localhost:5173")
// public class AdminController {

// 	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

// 	@Autowired
// 	private ProductsService productsService;

// 	@Autowired
// 	private categories categoriesService;

// 	@Autowired
// 	private SubCategoryService subCategoryService;

// 	@Autowired
// 	private ExcelService excelService;

// 	@PostMapping("/categories")
// 	public ResponseEntity<?> addCategory(@RequestBody AddCategoryDto addCategoryDto, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			Category category = categoriesService.addCategory(addCategoryDto);
// 			return ResponseEntity.ok(category);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error adding category: " + e.getMessage());
// 		}
// 	}

// 	@PutMapping("/categories/{id}")
// 	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody AddCategoryDto addCategoryDto,
// 			Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			Category category = categoriesService.updateCategory(id, addCategoryDto);
// 			if (category == null) {
// 				return ResponseEntity.notFound().build();
// 			}
// 			return ResponseEntity.ok(category);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error updating category: " + e.getMessage());
// 		}
// 	}

// 	@DeleteMapping("/categories/{id}")
// 	public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			categoriesService.deleteCategory(id);
// 			return ResponseEntity.ok("Category deleted successfully");
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error deleting category: " + e.getMessage());
// 		}
// 	}

// 	@PostMapping("/subcategories")
// 	public ResponseEntity<?> addSubCategory(@RequestBody AddSubCategoryDto dto, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}
// 		try {
// 			SubCategory subCategory = subCategoryService.addSubCategory(dto);
// 			return ResponseEntity.ok(subCategory);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error adding subcategory: " + e.getMessage());
// 		}
// 	}

// 	@PutMapping("/subcategories/{id}")
// 	public ResponseEntity<?> updateSubCategory(@PathVariable Long id, @RequestBody AddSubCategoryDto dto,
// 			Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}
// 		try {
// 			SubCategory sc = subCategoryService.updateSubCategory(id, dto);
// 			if (sc == null) {
// 				return ResponseEntity.notFound().build();
// 			}
// 			return ResponseEntity.ok(sc);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error updating subcategory: " + e.getMessage());
// 		}
// 	}

// 	@DeleteMapping("/subcategories/{id}")
// 	public ResponseEntity<?> deleteSubCategory(@PathVariable Long id, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}
// 		try {
// 			subCategoryService.deleteSubCategory(id);
// 			return ResponseEntity.ok("Subcategory deleted successfully");
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error deleting subcategory: " + e.getMessage());
// 		}
// 	}

// 	@PostMapping("/products")
// 	public ResponseEntity<?> addProduct(@RequestBody AddProductDto addProductDto, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			Products product = productsService.addProduct(addProductDto);
// 			return ResponseEntity.ok(product);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
// 		}
// 	}

// 	@PutMapping("/products/{id}")
// 	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody AddProductDto addProductDto,
// 			Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			Products product = productsService.updateProduct(id, addProductDto);
// 			if (product == null) {
// 				return ResponseEntity.notFound().build();
// 			}
// 			return ResponseEntity.ok(product);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
// 		}
// 	}

// 	@DeleteMapping("/products/{id}")
// 	public ResponseEntity<?> deleteProduct(@PathVariable Long id, Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		try {
// 			productsService.deleteProduct(id);
// 			return ResponseEntity.ok("Product deleted successfully");
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
// 		}
// 	}

// 	@PostMapping("/products/bulk-upload")
// 	public ResponseEntity<?> bulkUploadProducts(@RequestParam(value = "file", required = false) MultipartFile file,
// 			Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		logger.info("Bulk upload request received. File: {}", file != null ? file.getOriginalFilename() : "null");
// 		logger.info("Request content type: {}", authentication.getDetails());

// 		if (file == null) {
// 			logger.warn("No file uploaded in bulk upload request");
// 			return ResponseEntity.badRequest().body("No file uploaded. Please select an Excel file to upload. " +
// 					"Make sure you're using 'form-data' in Postman with key 'file' set to File type.");
// 		}

// 		if (file.isEmpty()) {
// 			logger.warn("Empty file uploaded: {}", file.getOriginalFilename());
// 			return ResponseEntity.badRequest().body("The uploaded file is empty. Please select a valid Excel file.");
// 		}

// 		String fileName = file.getOriginalFilename();
// 		if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
// 			logger.warn("Invalid file format uploaded: {}", fileName);
// 			return ResponseEntity.badRequest().body(
// 					"Invalid file format. Please upload an Excel file (.xlsx or .xls). Only Excel files are supported.");
// 		}

// 		logger.info("Processing bulk upload for file: {}", fileName);

// 		try {
// 			BulkUploadResponseDto response = excelService.processBulkProductUpload(file);
// 			logger.info("Bulk upload completed successfully. Processed: {}, Success: {}, Failed: {}",
// 					response.getTotalProcessed(), response.getSuccessCount(), response.getFailureCount());
// 			return ResponseEntity.ok(response);
// 		} catch (Exception e) {
// 			logger.error("Error processing bulk upload: {}", e.getMessage(), e);
// 			return ResponseEntity.badRequest().body("Error processing bulk upload: " + e.getMessage());
// 		}
// 	}

// 	@GetMapping("/products/bulk-upload/template")
// 	public ResponseEntity<?> downloadTemplate(Authentication authentication) {
// 		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
// 				.anyMatch(r -> r.equals("ROLE_ADMIN"));
// 		if (!isAdmin) {
// 			return ResponseEntity.status(403).body("Forbidden");
// 		}

// 		return ResponseEntity.ok("Excel Template Format:\n" +
// 				"Column A: Product Name (Required)\n" +
// 				"Column B: Image URL\n" +
// 				"Column C: Category ID (Required)\n" +
// 				"Column D: Sub Category ID\n" +
// 				"Column E: Unit (Required)\n" +
// 				"Column F: Stock (Required)\n" +
// 				"Column G: Price (Required)\n" +
// 				"Column H: Discount\n" +
// 				"Column I: Description\n" +
// 				"Column J: More Details\n" +
// 				"Column K: Publish (true/false, yes/no, 1/0)");
// 	}
// }

package Ayurvedh.ayurvedh.Controllers;

import Ayurvedh.ayurvedh.Services.CloudinaryImageService;
import Ayurvedh.ayurvedh.Services.ExcelService;
import Ayurvedh.ayurvedh.Services.ProductsService;
import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.Services.SubCategoryService;
import Ayurvedh.ayurvedh.dto.AddProductDto;
import Ayurvedh.ayurvedh.dto.AddCategoryDto;
import Ayurvedh.ayurvedh.dto.AddSubCategoryDto;
import Ayurvedh.ayurvedh.dto.BulkUploadResponseDto;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// import org.apache.commons.math3.stat.descriptive.summary.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private ProductsService productsService;

	@Autowired
	private categories categoriesService;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private ExcelService excelService;

	@Autowired
	private CloudinaryImageService cloudinaryImageService;

	// Helper method to check admin role
	private boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(r -> r.equals("ROLE_ADMIN"));
	}

	// ===== CATEGORY ENDPOINTS =====

	

	@GetMapping("/categories")
	public ResponseEntity<?> getAllCategories(Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			List<AddCategoryDto> categories = categoriesService.getAllCategories();
			System.out.println(categories.size());
			return ResponseEntity.ok(categories);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching categories: " + e.getMessage());
		}
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
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

	@PostMapping("/categories")
	public ResponseEntity<?> addCategory(@RequestBody AddCategoryDto addCategoryDto, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			Category category = categoriesService.addCategory(addCategoryDto);
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error adding category: " + e.getMessage());
		}
	}

	@PutMapping(value = "/categories/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCategory(
			@PathVariable Long id,
			@RequestParam("name") String name,
			@RequestParam(value = "image", required = false) MultipartFile image,
			Authentication authentication) {

		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			Category category = categoriesService.updateCategory(id, name, image);
			if (category == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating category: " + e.getMessage());
		}
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			categoriesService.deleteCategory(id);
			return ResponseEntity.ok("Category deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting category: " + e.getMessage());
		}
	}

	// @GetMapping("/categories/{categoryId}/subcategories/count")
	// public ResponseEntity<?> getSubCategoryCount(@PathVariable Long categoryId, Authentication authentication) {
	// 	if (!isAdmin(authentication)) {
	// 		return ResponseEntity.status(403).body("Forbidden");
	// 	}
	// 	try {
	// 		int count = subCategoryService.countSubCategoriesByCategory(categoryId);
	// 		return ResponseEntity.ok(count);
	// 	} catch (Exception e) {
	// 		return ResponseEntity.badRequest().body("Error fetching subcategory count: " + e.getMessage());
	// 	}
	// }

	// ===== SUBCATEGORY ENDPOINTS =====

	@GetMapping("/subcategories")
	public ResponseEntity<?> getAllSubCategories(Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
			return ResponseEntity.ok(subCategories);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategories: " +
					e.getMessage());
		}
	}

	@GetMapping("/subcategories/{id}")
	public ResponseEntity<?> getSubCategoryById(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			SubCategory subCategory = subCategoryService.getSubCategoryById(id);
			if (subCategory == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(subCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategory: " + e.getMessage());
		}
	}

	@GetMapping("/subcategories/category/{categoryId}")
	public ResponseEntity<?> getSubCategoriesByCategory(@PathVariable Long categoryId, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			List<SubCategory> subCategories = subCategoryService.getSubCategoriesByCategory(categoryId);
			return ResponseEntity.ok(subCategories);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching subcategories by category: " + e.getMessage());
		}
	}

	@PostMapping(value = "/subcategories", consumes = { "multipart/form-data" })
	public ResponseEntity<?> addSubCategory(
			@RequestParam("name") String name,
			@RequestParam("categoryId") Long categoryId,
			@RequestParam(value = "image", required = false) MultipartFile image,
			Authentication authentication) {

		if (!isAdmin(authentication))
			return ResponseEntity.status(403).body("Forbidden");

		String imageUrl = null;
		if (image != null && !image.isEmpty()) {
			Map uploadResult = cloudinaryImageService.uploadImage(image);
			System.out.println("Upload Result: " + uploadResult);
			imageUrl = uploadResult.get("secure_url") != null
					? uploadResult.get("secure_url").toString()
					: uploadResult.get("url").toString();
		}

		AddSubCategoryDto dto = new AddSubCategoryDto();
		dto.setName(name);
		dto.setCategoryId(categoryId);
		dto.setImage(imageUrl);

		SubCategory subCategory = subCategoryService.addSubCategory(dto);
		return ResponseEntity.ok(subCategory);
	}

	@PutMapping("/subcategories/{id}")
	public ResponseEntity<?> updateSubCategory(@PathVariable Long id, @RequestBody AddSubCategoryDto dto,
			Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			SubCategory sc = subCategoryService.updateSubCategory(id, dto);
			if (sc == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(sc);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating subcategory: " + e.getMessage());
		}
	}

	@DeleteMapping("/subcategories/{id}")
	public ResponseEntity<?> deleteSubCategory(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			subCategoryService.deleteSubCategory(id);
			return ResponseEntity.ok("Subcategory deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting subcategory: " + e.getMessage());
		}
	}

	// ===== PRODUCT ENDPOINTS =====

	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts(Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			List<Products> products = productsService.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching products: " + e.getMessage());
		}
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
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

	@PostMapping(value = "/product", consumes = { "multipart/form-data" })
	public ResponseEntity<?> addProduct(
			@RequestParam("name") String name,
			@RequestParam("categoryId") Long categoryId,
			@RequestParam(value = "subCategoryId", required = false) Long subCategoryId,
			@RequestParam("unit") String unit,
			@RequestParam("stock") Integer stock,
			@RequestParam("price") Double price,
			@RequestParam(value = "discount", required = false) Double discount,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "moreDetails", required = false) String moreDetails,
			@RequestParam(value = "publish", defaultValue = "true") boolean publish,
			@RequestParam(value = "image", required = false) MultipartFile image,
			Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
		}
		try {

			String imageUrl = null;
			if (image != null && !image.isEmpty()) {
				Map uploadResult = cloudinaryImageService.uploadImage(image);
				imageUrl = uploadResult.get("secure_url") != null
						? uploadResult.get("secure_url").toString()
						: uploadResult.get("url").toString();
				System.out.println("Image uploaded to: " + imageUrl);
			}

			AddProductDto dto = new AddProductDto();
			dto.setName(name);
			dto.setCategoryId(categoryId);
			dto.setSubCategoryId(subCategoryId);
			dto.setUnit(unit);
			dto.setStock(stock);
			dto.setPrice(price);
			dto.setDiscount(discount);
			dto.setDescription(description);
			dto.setMoreDetails(moreDetails);
			dto.setPublish(publish);
			dto.setImage(imageUrl);

			Products product = productsService.addProduct(dto);
			System.out.println("Product saved with ID: " + product.getId());

			return ResponseEntity.ok(product);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
		}
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody AddProductDto addProductDto,
			Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			Products product = productsService.updateProduct(id, addProductDto);
			if (product == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
		}
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id, Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			productsService.deleteProduct(id);
			return ResponseEntity.ok("Product deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
		}
	}


	@GetMapping("/products/count")
	public ResponseEntity<?> getProductCount(Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			long count = productsService.getProductCount();
			return ResponseEntity.ok(count);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching product count: " + e.getMessage());
		}
	}

	// ===== BULK UPLOAD ENDPOINTS =====

	@PostMapping("/products/bulk-upload")
	public ResponseEntity<?> bulkUploadProducts(@RequestParam(value = "file", required = false) MultipartFile file,
			Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		logger.info("Bulk upload request received. File: {}", file != null ? file.getOriginalFilename() : "null");
		logger.info("Request content type: {}", authentication.getDetails());
		if (file == null) {
			logger.warn("No file uploaded in bulk upload request");
			return ResponseEntity.badRequest().body("No file uploaded. Please select an Excel file to upload. " +
					"Make sure you're using 'form-data' in Postman with key 'file' set to File type.");
		}
		if (file.isEmpty()) {
			logger.warn("Empty file uploaded: {}", file.getOriginalFilename());
			return ResponseEntity.badRequest().body("The uploaded file is empty. Please select a valid Excel file.");
		}
		String fileName = file.getOriginalFilename();
		if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
			logger.warn("Invalid file format uploaded: {}", fileName);
			return ResponseEntity.badRequest().body(
					"Invalid file format. Please upload an Excel file (.xlsx or .xls). Only Excel files are supported.");
		}
		logger.info("Processing bulk upload for file: {}", fileName);
		try {
			BulkUploadResponseDto response = excelService.processBulkProductUpload(file);
			logger.info("Bulk upload completed successfully. Processed: {}, Success: {}, Failed: {}",
					response.getTotalProcessed(), response.getSuccessCount(), response.getFailureCount());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error processing bulk upload: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body("Error processing bulk upload: " + e.getMessage());
		}
	}

	@GetMapping("/products/bulk-upload/template")
	public ResponseEntity<?> downloadTemplate(Authentication authentication) {
		if (!isAdmin(authentication)) {
			return ResponseEntity.status(403).body("Forbidden");
		}
		return ResponseEntity.ok("Excel Template Format:\n" +
				"Column A: Product Name (Required)\n" +
				"Column B: Image URL\n" +
				"Column C: Category ID (Required)\n" +
				"Column D: Sub Category ID\n" +
				"Column E: Unit (Required)\n" +
				"Column F: Stock (Required)\n" +
				"Column G: Price (Required)\n" +
				"Column H: Discount\n" +
				"Column I: Description\n" +
				"Column J: More Details\n" +
				"Column K: Publish (true/false, yes/no, 1/0)");
	}
}
package Ayurvedh.ayurvedh.Controllers;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/admin")
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
	
	@GetMapping("/dashboard")
	public ResponseEntity<?> adminDashboard(Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		return ResponseEntity.ok("Welcome Admin");
	}

	
	@PostMapping("/categories")
	public ResponseEntity<?> addCategory(@RequestBody AddCategoryDto addCategoryDto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		try {
			Category category = categoriesService.addCategory(addCategoryDto);
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error adding category: " + e.getMessage());
		}
	}
	

	
	@PutMapping("/categories/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody AddCategoryDto addCategoryDto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		try {
			Category category = categoriesService.updateCategory(id, addCategoryDto);
			if (category == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(category);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating category: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		try {
			categoriesService.deleteCategory(id);
			return ResponseEntity.ok("Category deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting category: " + e.getMessage());
		}
	}
	
	@PostMapping("/subcategories")
	public ResponseEntity<?> addSubCategory(@RequestBody AddSubCategoryDto dto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			SubCategory subCategory = subCategoryService.addSubCategory(dto);
			return ResponseEntity.ok(subCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error adding subcategory: " + e.getMessage());
		}
	}
	
	
	@PutMapping("/subcategories/{id}")
	public ResponseEntity<?> updateSubCategory(@PathVariable Long id, @RequestBody AddSubCategoryDto dto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
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
	public ResponseEntity<?> deleteSubCategory(@PathVariable Long id, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		try {
			subCategoryService.deleteSubCategory(id);
			return ResponseEntity.ok("Subcategory deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting subcategory: " + e.getMessage());
		}
	}
	
	@PostMapping("/products")
	public ResponseEntity<?> addProduct(@RequestBody AddProductDto addProductDto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		try {
			Products product = productsService.addProduct(addProductDto);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
		}
	}
	
	
	@PutMapping("/products/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody AddProductDto addProductDto, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
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
	public ResponseEntity<?> deleteProduct(@PathVariable Long id, Authentication authentication){
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		try {
			productsService.deleteProduct(id);
			return ResponseEntity.ok("Product deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
		}
	}
	
	@PostMapping("/products/bulk-upload")
	public ResponseEntity<?> bulkUploadProducts(@RequestParam(value = "file", required = false) MultipartFile file, Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		logger.info("Bulk upload request received. File: {}", file != null ? file.getOriginalFilename() : "null");
		logger.info("Request content type: {}", authentication.getDetails());
		
		// Check if file is present
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
			return ResponseEntity.badRequest().body("Invalid file format. Please upload an Excel file (.xlsx or .xls). Only Excel files are supported.");
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
	
	// Alternative endpoint that accepts the file in the request body
	@PostMapping("/products/bulk-upload-v2")
	public ResponseEntity<?> bulkUploadProductsV2(@RequestParam(value = "file", required = false) MultipartFile file, 
	                                            @RequestParam(value = "excelFile", required = false) MultipartFile excelFile,
	                                            Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		// Try to get file from either parameter
		MultipartFile uploadFile = file != null ? file : excelFile;
		
		// Check if file is present
		if (uploadFile == null) {
			return ResponseEntity.badRequest().body("No file uploaded. Please select an Excel file to upload. " +
				"Use either 'file' or 'excelFile' as the parameter name.");
		}
		
		// Check if file is empty
		if (uploadFile.isEmpty()) {
			return ResponseEntity.badRequest().body("The uploaded file is empty. Please select a valid Excel file.");
		}
		
		String fileName = uploadFile.getOriginalFilename();
		if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
			return ResponseEntity.badRequest().body("Invalid file format. Please upload an Excel file (.xlsx or .xls). Only Excel files are supported.");
		}
		
		try {
			BulkUploadResponseDto response = excelService.processBulkProductUpload(uploadFile);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error processing bulk upload: " + e.getMessage());
		}
	}
	
	@GetMapping("/products/bulk-upload/template")
	public ResponseEntity<?> downloadTemplate(Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
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
	
	// Test endpoint to verify multipart file upload is working
	@PostMapping("/test-file-upload")
	public ResponseEntity<?> testFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(r -> r.equals("ROLE_ADMIN"));
		if(!isAdmin){
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		logger.info("Test file upload request received. File: {}", file != null ? file.getOriginalFilename() : "null");
		
		if (file == null) {
			return ResponseEntity.ok("Test endpoint working. No file uploaded. Use this to test multipart configuration.");
		}
		
		return ResponseEntity.ok("Test endpoint working. File received: " + file.getOriginalFilename() + 
			", Size: " + file.getSize() + " bytes, Content Type: " + file.getContentType());
	}
}

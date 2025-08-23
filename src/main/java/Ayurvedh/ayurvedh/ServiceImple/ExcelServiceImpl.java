package Ayurvedh.ayurvedh.ServiceImple;

import Ayurvedh.ayurvedh.Services.ExcelService;
import Ayurvedh.ayurvedh.Services.ProductsService;
import Ayurvedh.ayurvedh.Services.categories;
import Ayurvedh.ayurvedh.Services.SubCategoryService;
import Ayurvedh.ayurvedh.dto.AddProductDto;
import Ayurvedh.ayurvedh.dto.BulkUploadResponseDto;
import Ayurvedh.ayurvedh.entity.Category;
import Ayurvedh.ayurvedh.entity.SubCategory;
import Ayurvedh.ayurvedh.entity.Products;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

	@Autowired
	private ProductsService productsService;

    @Autowired
    private categories categoriesService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Override
    public BulkUploadResponseDto processBulkProductUpload(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int totalProcessed = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            logger.info("Excel file opened successfully. Sheet name: {}, Total rows: {}", 
                sheet.getSheetName(), sheet.getLastRowNum());
            
            // Validate header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getLastCellNum() < 11) {
                String errorMsg = "Excel file must have at least 11 columns. Found: " + 
                    (headerRow != null ? headerRow.getLastCellNum() : 0) + ". Please use the provided template.";
                logger.error(errorMsg);
                errors.add(errorMsg);
                return new BulkUploadResponseDto(0, 0, 0, errors);
            }
            
            logger.info("Header row validated. Columns found: {}", headerRow.getLastCellNum());
            
            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    logger.warn("Row {} is null, skipping", i + 1);
                    continue;
                }
                
                totalProcessed++;
                logger.info("Processing row {}: {}", i + 1, row.getRowNum());
                
                try {
                    AddProductDto productDto = extractProductFromRow(row);
                    if (productDto != null) {
                        logger.info("Product DTO created for row {}: {}", i + 1, productDto.getName());
                        logger.info("Product details - Category ID: {}, SubCategory ID: {}, Price: {}, Stock: {}", 
                            productDto.getCategoryId(), productDto.getSubCategoryId(), productDto.getPrice(), productDto.getStock());
                        
                        Products savedProduct = productsService.addProduct(productDto);
                        successCount++;
                        logger.info("Product added successfully for row {} with ID: {}", i + 1, savedProduct.getId());
                    }
                } catch (Exception e) {
                    String errorMsg = "Row " + (i + 1) + ": " + e.getMessage();
                    logger.error(errorMsg, e);
                    errors.add(errorMsg);
                }
            }
        } catch (IOException e) {
            String errorMsg = "Error reading Excel file: " + e.getMessage();
            logger.error(errorMsg, e);
            errors.add(errorMsg);
        }

        return new BulkUploadResponseDto(totalProcessed, successCount, totalProcessed - successCount, errors);
    }

    private AddProductDto extractProductFromRow(Row row) throws Exception {
        AddProductDto dto = new AddProductDto();
        
        // Extract data from each column
        dto.setName(getStringCellValue(row.getCell(0), "Product Name"));
        dto.setImage(getStringCellValue(row.getCell(1), "Image URL"));
        dto.setCategoryId(getLongCellValue(row.getCell(2), "Category ID"));
        dto.setSubCategoryId(getLongCellValue(row.getCell(3), "Sub Category ID"));
        dto.setUnit(getStringCellValue(row.getCell(4), "Unit"));
        dto.setStock(getIntCellValue(row.getCell(5), "Stock"));
        dto.setPrice(getIntCellValue(row.getCell(6), "Price"));
        dto.setDiscount(getIntCellValue(row.getCell(7), "Discount"));
        dto.setDescription(getStringCellValue(row.getCell(8), "Description"));
        dto.setMoreDetails(getStringCellValue(row.getCell(9), "More Details"));
        dto.setPublish(getBooleanCellValue(row.getCell(10), "Publish"));
        
        // Validate category and subcategory exist
        validateCategoryAndSubCategory(dto);
        
        return dto;
    }

    private String getStringCellValue(Cell cell, String fieldName) throws Exception {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Long getLongCellValue(Cell cell, String fieldName) throws Exception {
        if (cell == null) {
            throw new Exception(fieldName + " is required");
        }
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue();
            case STRING:
                try {
                    return Long.parseLong(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    throw new Exception(fieldName + " must be a valid number");
                }
            default:
                throw new Exception(fieldName + " must be a valid number");
        }
    }

    private int getIntCellValue(Cell cell, String fieldName) throws Exception {
        if (cell == null) {
            throw new Exception(fieldName + " is required");
        }
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    throw new Exception(fieldName + " must be a valid number");
                }
            default:
                throw new Exception(fieldName + " must be a valid number");
        }
    }

    private boolean getBooleanCellValue(Cell cell, String fieldName) throws Exception {
        if (cell == null) {
            return false; // Default to false if not specified
        }
        
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                String value = cell.getStringCellValue().trim().toLowerCase();
                if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
                    return true;
                } else if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
                    return false;
                } else {
                    throw new Exception(fieldName + " must be true/false, yes/no, or 1/0");
                }
            case NUMERIC:
                return cell.getNumericCellValue() == 1;
            default:
                return false;
        }
    }

    private void validateCategoryAndSubCategory(AddProductDto dto) throws Exception {
        
        if (dto.getCategoryId() != null) {
            logger.info("Validating category ID: {}", dto.getCategoryId());
            Category category = categoriesService.getCategoryById(dto.getCategoryId());
            if (category == null) {
                String errorMsg = "Category with ID " + dto.getCategoryId() + " does not exist";
                logger.error(errorMsg);
                throw new Exception(errorMsg);
            }
            logger.info("Category validated: {}", category.getName());
        }
        
        // Validate subcategory exists
        if (dto.getSubCategoryId() != null) {
            logger.info("Validating subcategory ID: {}", dto.getSubCategoryId());
            SubCategory subCategory = subCategoryService.getSubCategoryById(dto.getSubCategoryId());
            if (subCategory == null) {
                String errorMsg = "SubCategory with ID " + dto.getSubCategoryId() + " does not exist";
                logger.error(errorMsg);
                throw new Exception(errorMsg);
            }
            logger.info("Subcategory validated: {}", subCategory.getName());
        }
    }
}

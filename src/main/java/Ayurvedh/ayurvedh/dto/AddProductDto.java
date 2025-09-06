package Ayurvedh.ayurvedh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductDto {
    private String name;
    private String image;
    private Long categoryId;
    private Long subCategoryId;
    private String unit;
    private int stock;
    private double price;
    private double  discount;
    private String description;
    private String moreDetails;
    private boolean publish;
    // private String categoryName;
    // private String subCategoryName;
}
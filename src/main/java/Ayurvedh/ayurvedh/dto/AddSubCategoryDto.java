package Ayurvedh.ayurvedh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSubCategoryDto {
    private Long id;
    private String name;
    private String image;
    private Long categoryId;
    private String categoryName;
    
}

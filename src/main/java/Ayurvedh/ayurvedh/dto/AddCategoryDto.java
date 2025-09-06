package Ayurvedh.ayurvedh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDto {
    private String name;
    private String image;
    // private List<AddSubCategoryDto> subCategories;
    
}
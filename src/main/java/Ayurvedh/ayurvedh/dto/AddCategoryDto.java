package Ayurvedh.ayurvedh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDto {
    private Long id;
    private String name;
    private String image;
    
    
}
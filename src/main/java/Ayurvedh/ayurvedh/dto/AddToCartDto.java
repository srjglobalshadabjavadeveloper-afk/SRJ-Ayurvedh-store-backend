package Ayurvedh.ayurvedh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartDto {
    private Long id;
    private Long productId;
    private Long userId;
    private Integer quantity;
}



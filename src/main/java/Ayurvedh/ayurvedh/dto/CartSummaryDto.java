package Ayurvedh.ayurvedh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartSummaryDto {
    private int totalItems;
    private int subTotalAmount;
    private int totalDiscount;
    private int totalAmount;
}



package Ayurvedh.ayurvedh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDto {
	private String orderDetails;
	private String paymentId;
	private String paymentStatus;
	private String deliveryAddress;
	private String deliveryStatus;
	private int subTotalAmount;
	private int totalAmount;
	private String invoiceReceipt;
}



package Ayurvedh.ayurvedh.dto;

import lombok.Data;

@Data
public class AddressDto {
	private Long id;
	private String addressLine;
	private String city;
	private String state;
	private String pinCode;
	private String country;
	private String mobile;
}



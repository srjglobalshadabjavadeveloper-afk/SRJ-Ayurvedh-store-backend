package Ayurvedh.ayurvedh.Services;

import java.util.List;

import Ayurvedh.ayurvedh.dto.RegisterUserDto;
import Ayurvedh.ayurvedh.dto.AddressDto;
import Ayurvedh.ayurvedh.dto.CreateOrderDto;
import Ayurvedh.ayurvedh.entity.Users;
import Ayurvedh.ayurvedh.entity.Address;
import Ayurvedh.ayurvedh.entity.Order;

public interface UsersService {
	Users register(RegisterUserDto dto);

	void sendEmailVerificationOtp(String email);

	boolean verifyEmail(String email, String otp);

	void initiateForgotPassword(String email);

	boolean resetPassword(String email, String otp, String newPassword);

	Address addAddressForUser(String email, AddressDto addressDto);

	List<Address> getAddressesForUser(String email);

	Order createOrder(String email, CreateOrderDto dto);

	List<Order> getOrders(String email);

	Order getOrderByBusinessId(String email, String orderId);
}

package Ayurvedh.ayurvedh.Services;

import java.util.List;

import Ayurvedh.ayurvedh.dto.RegisterUserDto;
import Ayurvedh.ayurvedh.dto.AddressDto;
import Ayurvedh.ayurvedh.dto.CreateOrderDto;
import Ayurvedh.ayurvedh.dto.AddToCartDto;
import Ayurvedh.ayurvedh.entity.Users;
import Ayurvedh.ayurvedh.entity.Address;
import Ayurvedh.ayurvedh.entity.Order;
import Ayurvedh.ayurvedh.entity.CartProducts;
import Ayurvedh.ayurvedh.dto.CartSummaryDto;

public interface UsersService {
	Users register(RegisterUserDto dto);

	void sendEmailVerificationOtp(String email);

	boolean verifyEmail(String email, String otp);

	void initiateForgotPassword(String email);

	boolean resetPassword(String email, String otp, String newPassword);

	Address addAddressForUser(String email, AddressDto addressDto);

	List<Address> getAddressesForUser(String email);

	// Update an address
	Address updateAddress(String email, Long addressId, AddressDto addressDto);

	// Delete an address
	void deleteAddress(String email, Long addressId);

	Order createOrder(String email, CreateOrderDto dto);

	List<Order> getOrders(String email);

	Order getOrderByBusinessId(String email, String orderId);

	CartProducts addToCart(String email, AddToCartDto dto);

	java.util.List<CartProducts> getCart(String email);

	CartProducts updateCartItemQuantity(String email, Long cartItemId, Integer quantity);

	void removeCartItem(String email, Long cartItemId);

	void clearCart(String email);

	CartSummaryDto getCartSummary(String email);

}

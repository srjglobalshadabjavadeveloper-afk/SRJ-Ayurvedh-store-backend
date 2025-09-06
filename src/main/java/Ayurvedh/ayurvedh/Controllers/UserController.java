package Ayurvedh.ayurvedh.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import Ayurvedh.ayurvedh.Services.UsersService;
import Ayurvedh.ayurvedh.dto.AddressDto;
import Ayurvedh.ayurvedh.entity.Address;
import Ayurvedh.ayurvedh.entity.Order;
import Ayurvedh.ayurvedh.dto.CreateOrderDto;
import Ayurvedh.ayurvedh.dto.AddToCartDto;
import Ayurvedh.ayurvedh.entity.CartProducts;
import Ayurvedh.ayurvedh.dto.CartSummaryDto;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }


    @PostMapping("/addresses")
    public ResponseEntity<Long> addAddress(Authentication authentication, @RequestBody AddressDto dto) {
        String email = authentication.getName();
        Address saved = usersService.addAddressForUser(email, dto);
        return ResponseEntity.ok(saved.getId());
    }   

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses(Authentication authentication) {
        String email = authentication.getName();
        List<AddressDto> addresses = usersService.getAddressesForUser(email).stream().map(addr -> {
            AddressDto d = new AddressDto();
            d.setAddressLine(addr.getAddress_line());
            d.setCity(addr.getCity());
            d.setState(addr.getState());
            d.setPinCode(addr.getPin_code());
            d.setCountry(addr.getCountry());
            d.setMobile(String.valueOf(addr.getMobile()));
            return d;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(Authentication authentication, @RequestBody CreateOrderDto dto) {
        String email = authentication.getName();
        Order order = usersService.createOrder(email, dto);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(Authentication authentication) {
        String email = authentication.getName();
        List<Order> orders = usersService.getOrders(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(Authentication authentication,
            @org.springframework.web.bind.annotation.PathVariable String orderId) {
        String email = authentication.getName();
        Order order = usersService.getOrderByBusinessId(email, orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/cart")
    public ResponseEntity<CartProducts> addToCart(Authentication authentication, @RequestBody AddToCartDto dto) {
        String email = authentication.getName();
        CartProducts cp = usersService.addToCart(email, dto);
        return ResponseEntity.ok(cp);
    }

    @GetMapping("/cart")
    public ResponseEntity<java.util.List<CartProducts>> getCart(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(usersService.getCart(email));
    }

    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<CartProducts> updateCartItem(Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody java.util.Map<String, Integer> body) {
        String email = authentication.getName();
        Integer quantity = body.get("quantity");
        CartProducts updated = usersService.updateCartItemQuantity(email, cartItemId, quantity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(Authentication authentication, @PathVariable Long cartItemId) {
        String email = authentication.getName();
        usersService.removeCartItem(email, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String email = authentication.getName();
        usersService.clearCart(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart/summary")
    public ResponseEntity<CartSummaryDto> getCartSummary(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(usersService.getCartSummary(email));
    }
}

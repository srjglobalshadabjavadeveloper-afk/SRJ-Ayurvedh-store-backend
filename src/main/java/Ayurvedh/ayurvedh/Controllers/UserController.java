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
    public ResponseEntity<?> addAddress(@RequestBody AddressDto dto, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        try {
            Address saved = usersService.addAddressForUser(email, dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        List<AddressDto> addresses = usersService.getAddressesForUser(email).stream()
                .map(addr -> {
                    AddressDto d = new AddressDto();
                    d.setId(addr.getId());
                    d.setAddressLine(addr.getAddressLine());
                    d.setCity(addr.getCity());
                    d.setState(addr.getState());
                    d.setPinCode(addr.getPinCode());
                    d.setCountry(addr.getCountry());
                    d.setMobile(String.valueOf(addr.getMobile()));
                    return d;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody AddressDto dto) {

        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName(); // get logged-in user email
        try {
            Address updated = usersService.updateAddress(email, id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        usersService.deleteAddress(email, id);
        return ResponseEntity.ok("Address deleted successfully");
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

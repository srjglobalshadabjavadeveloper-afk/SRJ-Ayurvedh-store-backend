package Ayurvedh.ayurvedh.ServiceImple;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Ayurvedh.ayurvedh.Repositories.RegistrationRepo;
import Ayurvedh.ayurvedh.Repositories.AddressRepository;
import Ayurvedh.ayurvedh.Repositories.RolesRepository;
import Ayurvedh.ayurvedh.Services.UsersService;
import Ayurvedh.ayurvedh.dto.RegisterUserDto;
import Ayurvedh.ayurvedh.dto.AddressDto;
import Ayurvedh.ayurvedh.dto.CreateOrderDto;
import Ayurvedh.ayurvedh.dto.AddToCartDto;
import Ayurvedh.ayurvedh.entity.Address;
import Ayurvedh.ayurvedh.entity.Order;
import Ayurvedh.ayurvedh.entity.Users;
import Ayurvedh.ayurvedh.entity.Roles;
import Ayurvedh.ayurvedh.entity.CartProducts;
import Ayurvedh.ayurvedh.entity.Products;
import Ayurvedh.ayurvedh.Repositories.OrderRepository;
import Ayurvedh.ayurvedh.Repositories.CartProductsRepository;
import Ayurvedh.ayurvedh.Repositories.ProductsRepository;
import Ayurvedh.ayurvedh.dto.CartSummaryDto;

@Service
public class UsersServiceImpl implements UsersService {

    private final RegistrationRepo registrationRepo;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final OrderRepository orderRepository;
    private final RolesRepository rolesRepository;
    private final CartProductsRepository cartProductsRepository;
    private final ProductsRepository productsRepository;

    public UsersServiceImpl(RegistrationRepo registrationRepo, AddressRepository addressRepository,
            PasswordEncoder passwordEncoder, MailService mailService,
            OrderRepository orderRepository, RolesRepository rolesRepository,
            CartProductsRepository cartProductsRepository, ProductsRepository productsRepository) {
        this.registrationRepo = registrationRepo;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.orderRepository = orderRepository;
        this.rolesRepository = rolesRepository;
        this.cartProductsRepository = cartProductsRepository;
        this.productsRepository = productsRepository;
    }

    @Override
    @Transactional
    public Users register(RegisterUserDto dto) {
        Users existing = registrationRepo.findByEmail(dto.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        Roles userRole = rolesRepository.findByName("USER")
                .orElseGet(() -> {
                    Roles newRole = new Roles();
                    newRole.setName("USER");
                    newRole.setDescription("Regular user role");
                    newRole.setCreatedAt(new Date());
                    newRole.setUpdatedAt(new Date());
                    return rolesRepository.save(newRole);
                });

        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(userRole); // Automatically assign USER role
        user.setStatus(false);
        user.setVerify_email(false);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        registrationRepo.save(user);

        sendEmailVerificationOtp(dto.getEmail());
        return user;
    }

    @Override
    @Transactional
    public Order createOrder(String email, CreateOrderDto dto) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrder_id("ORD-" + System.currentTimeMillis());
        order.setOrder_details(dto.getOrderDetails());
        order.setPayment_id(dto.getPaymentId());
        order.setPayment_status(dto.getPaymentStatus());
        order.setDelivery_address(dto.getDeliveryAddress());
        order.setDelivery_status(dto.getDeliveryStatus());
        order.setSub_total_amount(dto.getSubTotalAmount());
        order.setTotal_amount(dto.getTotalAmount());
        order.setInvoice_receipt(dto.getInvoiceReceipt());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public CartProducts addToCart(String email, AddToCartDto dto) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (dto == null || dto.getProductId() == null || dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid request");
        }
        Products product = productsRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        CartProducts cartProduct = cartProductsRepository
                .findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);
        if (cartProduct == null) {
            cartProduct = new CartProducts();
            cartProduct.setUser(user);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(dto.getQuantity());
            cartProduct.setCreatedAt(new Date());
            cartProduct.setUpdatedAt(new Date());
        } else {
            cartProduct.setQuantity(cartProduct.getQuantity() + dto.getQuantity());
            cartProduct.setUpdatedAt(new Date());
        }
        CartProducts saved = cartProductsRepository.save(cartProduct);
        return saved;
    }

    // @Override
    // public java.util.List<CartProducts> getCart(String email) {
    // Users user = registrationRepo.findByEmail(email);
    // if (user == null) {
    // throw new IllegalArgumentException("User not found");
    // }
    // return cartProductsRepository.findByUserId(user.getId());
    // }

    @Override
    public List<CartProducts> getCart(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<CartProducts> cartProducts = cartProductsRepository.findByUserId(user.getId());
        // Map CartProducts → CartDto
        List<CartProducts> cartDtos = cartProducts.stream().map(cp -> {
            CartProducts dto = new CartProducts();
            dto.setId(cp.getId());
            dto.setProduct(cp.getProduct());;
            dto.setUser(cp.getUser());
            dto.setQuantity(cp.getQuantity());
            return dto;
        }).collect(Collectors.toList());
        return cartDtos;
    }

    @Override
    @Transactional
    public CartProducts updateCartItemQuantity(String email, Long cartItemId, Integer quantity) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        CartProducts item = cartProductsRepository.findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        item.setQuantity(quantity);
        item.setUpdatedAt(new Date());
        return cartProductsRepository.save(item);
    }

    @Override
    @Transactional
    public void removeCartItem(String email, Long cartItemId) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        CartProducts item = cartProductsRepository.findByIdAndUserId(cartItemId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cartProductsRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        cartProductsRepository.deleteByUserId(user.getId());
    }

    @Override
    public CartSummaryDto getCartSummary(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        java.util.List<CartProducts> items = cartProductsRepository.findByUserId(user.getId());
        CartSummaryDto summary = new CartSummaryDto();
        int totalItems = 0;
        int subTotal = 0;
        int discountTotal = 0;
        for (CartProducts cp : items) {
            int qty = cp.getQuantity();
            totalItems += qty;
            double price = cp.getProduct().getPrice();
            double discount = cp.getProduct().getDiscount();
            double lineSubTotal = price * qty;
            double lineDiscount = discount * qty;
            subTotal += lineSubTotal;
            discountTotal += lineDiscount;
        }
        summary.setTotalItems(totalItems);
        summary.setSubTotalAmount(subTotal);
        summary.setTotalDiscount(discountTotal);
        summary.setTotalAmount(subTotal - discountTotal);
        return summary;
    }

    @Override
    public java.util.List<Order> getOrders(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return orderRepository.findByUserId(user.getId());
    }

    @Override
    public Order getOrderByBusinessId(String email, String orderId) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) {
            return null;
        }
        if (!user.getId().equals(order.getUser().getId())) {
            throw new IllegalArgumentException("Forbidden");
        }
        return order;
    }

    @Override
    @Transactional
    public Address addAddressForUser(String email, AddressDto addressDto) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Address address = new Address();
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPinCode(addressDto.getPinCode());
        address.setCountry(addressDto.getCountry());
        if (addressDto.getMobile() != null) {
            address.setMobile((addressDto.getMobile()));
        }

        address.setCreatedAt(new Date());
        address.setUpdatedAt(new Date());
        address.setUser(user);
        Address saved = addressRepository.save(address);
        user.addAddress(saved);
        registrationRepo.save(user);
        return saved;
    }

    @Override
    public Address updateAddress(String email, Long addressId, AddressDto dto) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to user");
        }

        // Update fields
        address.setAddressLine(dto.getAddressLine());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPinCode(dto.getPinCode());
        address.setCountry(dto.getCountry());
        address.setMobile(dto.getMobile());
        address.setUpdatedAt(new Date());

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(String email, Long addressId) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to user");
        }

        addressRepository.delete(address);
    }

    @Override
    public java.util.List<Address> getAddressesForUser(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user.getAddresses();
    }

    @Override
    public void sendEmailVerificationOtp(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null)
            return;
        String otp = generateOtp();
        user.setEmailVerificationOtp(otp);
        user.setEmailVerificationExpiry(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)));
        registrationRepo.save(user);
        mailService.sendMail(email, "Verify your email", "Your verification OTP is: " + otp);
    }

    @Override
    @Transactional
    public boolean verifyEmail(String email, String otp) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null)
            return false;
        if (user.getEmailVerificationOtp() == null || user.getEmailVerificationExpiry() == null)
            return false;
        if (user.getEmailVerificationExpiry().before(new Date()))
            return false;
        boolean matched = otp != null && otp.equals(user.getEmailVerificationOtp());
        if (matched) {
            user.setVerify_email(true);
            user.setStatus(true);
            user.setEmailVerificationOtp(null);
            user.setEmailVerificationExpiry(null);
            registrationRepo.save(user);
        }
        return matched;
    }

    @Override
    public void initiateForgotPassword(String email) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null)
            return;
        String otp = generateOtp();
        user.setResetPasswordOtp(otp);
        user.setResetPasswordExpiry(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)));
        registrationRepo.save(user);
        mailService.sendMail(email, "Password reset OTP", "Your password reset OTP is: " + otp);
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String otp, String newPassword) {
        Users user = registrationRepo.findByEmail(email);
        if (user == null)
            return false;
        if (user.getResetPasswordOtp() == null || user.getResetPasswordExpiry() == null)
            return false;
        if (user.getResetPasswordExpiry().before(new Date()))
            return false;
        boolean matched = otp != null && otp.equals(user.getResetPasswordOtp());
        if (matched) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setLastPasswordReset(new Date());
            user.setResetPasswordOtp(null);
            user.setResetPasswordExpiry(null);
            registrationRepo.save(user);
        }
        return matched;
    }

    private String generateOtp() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }
}

package Ayurvedh.ayurvedh.Controllers;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import Ayurvedh.ayurvedh.Services.UsersService;
import Ayurvedh.ayurvedh.Configurations.JwtUtil;
import Ayurvedh.ayurvedh.Repositories.RegistrationRepo;
import Ayurvedh.ayurvedh.entity.Users;
import Ayurvedh.ayurvedh.dto.RegisterUserDto;
import lombok.Data;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RegistrationRepo registrationRepo;

    public AuthController(UsersService usersService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, RegistrationRepo registrationRepo) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.registrationRepo = registrationRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto req) {
        try {
            usersService.register(req);
            return ResponseEntity.ok("Registered. Please verify OTP sent to email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest req) {
        boolean ok = usersService.verifyEmail(req.getEmail(), req.getOtp());
        return ok ? ResponseEntity.ok("Email verified") : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest req) {
        usersService.initiateForgotPassword(req.getEmail());
        return ResponseEntity.ok("Password reset OTP sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest req) {
        boolean ok = usersService.resetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
        return ok ? ResponseEntity.ok("Password updated") : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        if (req == null || req.getEmail() == null || req.getEmail().isBlank() || req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            // Update last login time without loading the full user object
            registrationRepo.updateLastLoginByEmail(req.getEmail(), new Date());

            String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

            String token = jwtUtil.generateToken(req.getEmail());
            long expiresInMs = jwtUtil.getExpirationMs();
            return ResponseEntity.ok(new LoginResponse(role, token, expiresInMs));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Data
    public static class VerifyRequest {
        private String email;
        private String otp;
    }

    @Data
    public static class ForgotPasswordRequest {
        private String email;
    }

    @Data
    public static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private final String role;
        private final String token;
        private final long expiresInMs;
    }
}



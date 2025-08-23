package Ayurvedh.ayurvedh.ServiceImple;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ayurvedh.ayurvedh.Repositories.RegistrationRepo;
import Ayurvedh.ayurvedh.entity.Users;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final RegistrationRepo registrationRepo;

    public UserDetailsServiceImpl(RegistrationRepo registrationRepo) {
        this.registrationRepo = registrationRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user details for email: {}", email);
        
        Users user = registrationRepo.findByEmailWithRole(email);
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            throw new UsernameNotFoundException("User not found");
        }
        
        // Simple role handling - default to USER if role is missing
        String role = "USER";
        try {
            if (user.getRole() != null && user.getRole().getName() != null) {
                role = user.getRole().getName().toUpperCase();
            }
        } catch (Exception e) {
            logger.warn("Error accessing role for user {}, using default USER role: {}", email, e.getMessage());
            role = "USER";
        }
        
        logger.info("User found: ID={}, Email={}, Role={}", user.getId(), user.getEmail(), role);
        
        boolean enabled = user.isVerify_email() && user.isStatus();
        
        logger.info("Setting role: {}, Enabled: {}, Verified: {}, Status: {}", 
            role, enabled, user.isVerify_email(), user.isStatus());
        
        UserDetails userDetails = new User(
            user.getEmail(),
            user.getPassword(),
            enabled,
            true,
            true,
            user.isStatus(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
        );
        
        logger.info("Created UserDetails with authorities: {}", userDetails.getAuthorities());
        
        return userDetails;
    }
}



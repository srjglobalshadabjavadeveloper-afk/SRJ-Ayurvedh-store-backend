package Ayurvedh.ayurvedh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import Ayurvedh.ayurvedh.Repositories.RegistrationRepo;
import Ayurvedh.ayurvedh.Repositories.RolesRepository;
import Ayurvedh.ayurvedh.entity.Users;
import Ayurvedh.ayurvedh.entity.Roles;

@SpringBootApplication
public class AyurvedhApplication {

	public static void main(String[] args) {
		SpringApplication.run(AyurvedhApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDefaultAdmin(RegistrationRepo registrationRepo, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			final String adminEmail = "admin@ayurvedh.local";
			final String adminPassword = "admin123";
			
			// Create roles if they don't exist
			Roles adminRole = getOrCreateRole(rolesRepository, "ADMIN", "Administrator role");
			Roles userRole = getOrCreateRole(rolesRepository, "USER", "Regular user role");
			
			
			Users existingAdmin = registrationRepo.findByEmail(adminEmail);
			if (existingAdmin == null) {
				
				Users admin = new Users();
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode(adminPassword));
				admin.setName("System Administrator");
				admin.setVerify_email(true);
				admin.setStatus(true);
				admin.setRole(adminRole);
				admin.setCreatedAt(new Date());
				admin.setUpdatedAt(new Date());
				admin.setAvatar("ADMIN");
				
				registrationRepo.save(admin);
				System.out.println("✅ Default admin user created successfully!");
				System.out.println("   Email: " + adminEmail);
				System.out.println("   Password: " + adminPassword);
				System.out.println("   Role: ADMIN");
			} else {
				// Fix existing admin user if needed
				if (existingAdmin.getRole() == null || !"ADMIN".equals(existingAdmin.getRole().getName())) {
					existingAdmin.setRole(adminRole);
					existingAdmin.setUpdatedAt(new Date());
					registrationRepo.save(existingAdmin);
					System.out.println(" Existing admin user role updated to ADMIN");
				} else {
					System.out.println("  Admin user already exists with correct ADMIN role");
				}
			}
		};
	}
	
	private Roles getOrCreateRole(RolesRepository rolesRepository, String roleName, String description) {
		return rolesRepository.findByName(roleName)
			.orElseGet(() -> {
				Roles newRole = new Roles();
				newRole.setName(roleName);
				newRole.setDescription(description);
				newRole.setCreatedAt(new Date());
				newRole.setUpdatedAt(new Date());
				return rolesRepository.save(newRole);
			});
	}
}

package Ayurvedh.ayurvedh.ServiceImple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Ayurvedh.ayurvedh.Repositories.RolesRepository;
import Ayurvedh.ayurvedh.entity.Roles;

import java.util.Date;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Initialize basic roles if they don't exist
        initializeRoles();
    }

    private void initializeRoles() {
        // Create USER role if it doesn't exist
        if (!rolesRepository.existsByName("USER")) {
            Roles userRole = new Roles();
            userRole.setName("USER");
            userRole.setDescription("Regular user role");
            userRole.setCreatedAt(new Date());
            userRole.setUpdatedAt(new Date());
            rolesRepository.save(userRole);
        }

        // Create ADMIN role if it doesn't exist
        if (!rolesRepository.existsByName("ADMIN")) {
            Roles adminRole = new Roles();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator role");
            adminRole.setCreatedAt(new Date());
            adminRole.setUpdatedAt(new Date());
            rolesRepository.save(adminRole);
        }
    }
}

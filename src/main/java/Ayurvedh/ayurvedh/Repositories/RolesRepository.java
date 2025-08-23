package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ayurvedh.ayurvedh.entity.Roles;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    
    /**
     * Find a role by its name
     * @param name the role name to search for
     * @return Optional containing the role if found
     */
    Optional<Roles> findByName(String name);
    
    /**
     * Check if a role with the given name exists
     * @param name the role name to check
     * @return true if the role exists, false otherwise
     */
    boolean existsByName(String name);
}

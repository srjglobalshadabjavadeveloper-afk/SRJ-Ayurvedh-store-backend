package Ayurvedh.ayurvedh.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import Ayurvedh.ayurvedh.entity.Users;
import java.util.Date;

public interface RegistrationRepo extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.role WHERE u.email = :email")
    Users findByEmailWithRole(@Param("email") String email);
    
    @Modifying
    @Query("UPDATE Users u SET u.lastLogin = :lastLogin WHERE u.email = :email")
    void updateLastLoginByEmail(@Param("email") String email, @Param("lastLogin") Date lastLogin);
}

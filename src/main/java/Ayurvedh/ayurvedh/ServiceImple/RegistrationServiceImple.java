package Ayurvedh.ayurvedh.ServiceImple;

import org.springframework.stereotype.Service;

import Ayurvedh.ayurvedh.Services.RegistrationService;
import Ayurvedh.ayurvedh.entity.Users;

@Service
public class RegistrationServiceImple implements RegistrationService {

    @Override
    public String register(Users user) {
        return "User registered successfully";
    }
}

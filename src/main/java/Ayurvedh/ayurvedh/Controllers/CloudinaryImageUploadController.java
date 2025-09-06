package Ayurvedh.ayurvedh.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Ayurvedh.ayurvedh.Services.CloudinaryImageService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin")
public class CloudinaryImageUploadController {


    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile image ) {

        // Assuming 'file' is obtained from the request
        // MultipartFile file = ...;
         Map uploadResult = cloudinaryImageService.uploadImage(image);
         return ResponseEntity.ok(uploadResult);
    }
}

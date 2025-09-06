package Ayurvedh.ayurvedh.ServiceImple;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import Ayurvedh.ayurvedh.Services.CloudinaryImageService;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    @Autowired
     private Cloudinary cloudinary;

      public Map uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }
    }
}

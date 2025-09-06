package Ayurvedh.ayurvedh.Services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryImageService {

    public Map uploadImage(MultipartFile file);

}

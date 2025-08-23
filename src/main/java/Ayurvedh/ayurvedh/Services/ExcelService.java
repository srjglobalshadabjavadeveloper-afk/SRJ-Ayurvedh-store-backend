package Ayurvedh.ayurvedh.Services;

import Ayurvedh.ayurvedh.dto.BulkUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    BulkUploadResponseDto processBulkProductUpload(MultipartFile file);
}

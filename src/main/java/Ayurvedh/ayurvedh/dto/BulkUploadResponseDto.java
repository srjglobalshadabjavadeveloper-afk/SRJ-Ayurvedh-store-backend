package Ayurvedh.ayurvedh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadResponseDto {
    private int totalProcessed;
    private int successCount;
    private int failureCount;
    private List<String> errors;
    private String message;
    
    public BulkUploadResponseDto(int totalProcessed, int successCount, int failureCount, List<String> errors) {
        this.totalProcessed = totalProcessed;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errors = errors;
        this.message = String.format("Processed %d products. %d successful, %d failed.", totalProcessed, successCount, failureCount);
    }
}

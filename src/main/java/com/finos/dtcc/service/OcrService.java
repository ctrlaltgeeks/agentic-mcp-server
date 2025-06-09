package com.finos.dtcc.service;

import com.finos.dtcc.enums.DocumentType;
import com.finos.dtcc.entity.KycDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OcrService {

    public KycDetails process(String clientId) {
        log.info("KYC document processing for client: {}", clientId);
        try {
            Thread.sleep(1000); // Simulating OCR processing delay
            log.info("OCR processing completed for the client: {}", clientId);
            return KycDetails.builder()
                    .documentType(DocumentType.AADHAR)
                    .documentId("123456789001")
                    .fullName("John Doe")
                    .dateOfBirth("01-01-1990")
                    .gender("Male")
                    .address("Kharadi, Pune, India")
                    .build();
        } catch (InterruptedException e) {
            log.error("Error processing image: {}", e.getMessage());
            return null;
        }
    }

}

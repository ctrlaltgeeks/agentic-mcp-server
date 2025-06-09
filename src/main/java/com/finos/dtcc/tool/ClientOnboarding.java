package com.finos.dtcc.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.finos.dtcc.entity.Client;
import com.finos.dtcc.enums.OnboardingStatus;
import com.finos.dtcc.model.response.OnboardingResponse;
import com.finos.dtcc.repository.ClientRepository;
import com.finos.dtcc.service.OcrService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClientOnboarding {

    private final OcrService ocrService;

    private final ClientRepository clientRepository;

    public ClientOnboarding(OcrService ocrService, ClientRepository clientRepository) {
        this.ocrService = ocrService;
        this.clientRepository = clientRepository;
    }

    /**
     * This method handles the onboarding process for a user.
     * It processes the images provided in the onboarding request using the OCR
     * service.
     *
     * @param clientId The ID of the client to be onboarded.
     * @return An OnboardingResponse indicating the status of the onboarding
     *         process.
     */
    @Tool(name = "Client Onboarding", description = "Handles client onboarding by processing KYC documents using OCR service.")
    public OnboardingResponse onboard(String clientId) {
        log.info("Onboarding user with ID: {}", clientId);

        // Process the images using OCR service
        var kycDetails = ocrService.process(clientId);
        if (kycDetails == null) {
            log.error("OCR processing failed for user ID: {}", clientId);
            return new OnboardingResponse(clientId, OnboardingStatus.FAILED,
                    "Failed to process the request, please try again later.");
        } else {
            log.info("OCR processing successful for user ID: {}", clientId);
            clientRepository.save(new Client(clientId, List.of(kycDetails)));
            log.info("Client with ID: {} has been successfully onboarded.", clientId);
            return new OnboardingResponse(clientId, OnboardingStatus.SUCCESS);
        }

    }
}
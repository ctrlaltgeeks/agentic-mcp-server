package com.finos.dtcc.tool;

import com.finos.dtcc.entity.Client;
import com.finos.dtcc.enums.OnboardingStatus;
import com.finos.dtcc.model.response.OnboardingResponse;
import com.finos.dtcc.repository.ClientRepository;
import com.finos.dtcc.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ClientTool {

    private final OcrService ocrService;

    private final ClientRepository clientRepository;

    public ClientTool(OcrService ocrService, ClientRepository clientRepository) {
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
     * process.
     */
    @Tool(name = "ClientOnboarding", description = "Handles client onboarding by processing KYC documents using OCR service.")
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
            Client client = Client.builder()
                    .id(clientId)
                    .kycDetails(List.of(kycDetails))
                    .build();
            clientRepository.save(client);
            log.info("Client with ID: {} has been successfully onboarded.", clientId);
            return new OnboardingResponse(clientId, OnboardingStatus.SUCCESS);
        }

    }

    /**
     * This method retrieves client details by client ID.
     *
     * @param clientId The ID of the client to retrieve.
     * @return The Client object containing the client's details.
     */
    @Tool(name = "GetClient", description = "Retrieves client details by client ID.")
    public Client getClient(String clientId) {
        log.info("Retrieving client with ID: {}", clientId);
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + clientId));
    }
}
package pl.kaminski.powerscore.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.kaminski.powerscore.client.application.contract.PersonalDataRequest;
import pl.kaminski.powerscore.verification.application.contract.*;
import pl.kaminski.powerscore.verification.query.contract.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VerificationController {

    private final IVerificationService service;

    @PostMapping("/verification")
    public CreateVerificationResult createVerification(@RequestBody CreateVerificationRequest request) {
        return service.createVerification(request);
    }

    @PostMapping("/verifications/{id}/personalData")
    public AddPersonalDataResult addPersonalData(@PathVariable UUID id, @RequestBody PersonalDataRequest request) {
        return service.addPersonalData(id, request);
    }

    @Operation(summary = "Upload document image")
    @PostMapping(value = "/verifications/{id}/documentImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadDocumentImageResult uploadDocumentImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            return service.uploadDocumentImage(id, file.getBytes(), file.getContentType());
        } catch (IOException e) {
            return UploadDocumentImageResult.imageEmpty();
        }
    }

    @PostMapping("/verifications/{id}/bankingReportApproval")
    public HandleBankingReportApprovalResult provideBankingReportApproval(@PathVariable UUID id, @RequestBody HandleBankingReportApprovalRequest request) {
        return service.provideBankingReportApproval(id, request);
    }

}

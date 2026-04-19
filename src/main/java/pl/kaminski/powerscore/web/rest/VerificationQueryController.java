package pl.kaminski.powerscore.web.rest;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.verification.application.contract.*;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.contract.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VerificationQueryController {

    private final IVerificationQueryFacade verificationQueryFacade;


    @GetMapping("/verifications/byDocument")
    public GetVerificationByDocumentResult getVerificationByDocument(@RequestParam("type") IdentityDocumentType type,
                                                                     @RequestParam("number") String number) {
        return verificationQueryFacade.getVerificationByDocument(type, number);
    }

    @GetMapping("/verifications/{id}")
    public GetVerificationResult getVerification(@PathVariable UUID id) {
        return verificationQueryFacade.getVerification(id);
    }

    @GetMapping("/verifications/{id}/documentImage")
    public ResponseEntity<byte[]> getDocumentImage(@PathVariable UUID id) {
        var result = verificationQueryFacade.getDocumentImage(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Można by to dynamicznie pobierać, ale na razie JPEG jest bezpieczny
                    .body(result.getSuccess());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Lub obsłużyć błędy z Result
    }

    @GetMapping("/verifications")
    public PaginatedResponse<VerificationInfo> getAllVerifications(@ParameterObject VerificationFilter verificationFilters) {
        return verificationQueryFacade.getAllVerifications(verificationFilters);
    }


}

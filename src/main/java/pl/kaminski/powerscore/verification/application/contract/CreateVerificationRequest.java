package pl.kaminski.powerscore.verification.application.contract;

import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;

public record CreateVerificationRequest(IdentityDocumentType type, String number) {
}

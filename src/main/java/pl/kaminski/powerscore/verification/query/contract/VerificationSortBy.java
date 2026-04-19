package pl.kaminski.powerscore.verification.query.contract;

import lombok.Getter;
import pl.kaminski.powerscore.client.query.QClient_;
import pl.kaminski.powerscore.verification.query.QIdentityDocument_;
import pl.kaminski.powerscore.verification.query.QVerification_;

@Getter
public enum VerificationSortBy {
    VERIFICATION_STATUS(QVerification_.STATUS),
    FIRST_NAME(QVerification_.CLIENT + "." + QClient_.FIRST_NAME),
    LAST_NAME(QVerification_.CLIENT + "." + QClient_.LAST_NAME),
    BIRTHDATE(QVerification_.CLIENT + "." + QClient_.BIRTHDATE),
    NATIONALITY(QVerification_.CLIENT + "." + QClient_.NATIONALITY),
    EMAIL(QVerification_.CLIENT + "." + QClient_.EMAIL),
    PHONE_NUMBER(QVerification_.CLIENT + "." + QClient_.PHONE_NUMBER),
    IDENTITY_DOCUMENT_TYPE(QVerification_.IDENTITY_DOCUMENT + "." + QIdentityDocument_.TYPE),
    IDENTITY_DOCUMENT_NUMBER(QVerification_.IDENTITY_DOCUMENT + "." + QIdentityDocument_.NUMBER),
    START_DATE(QVerification_.START_DATE),
    FINISH_DATE(QVerification_.FINISH_DATE);

    private final String fieldName;

    VerificationSortBy(String fieldName) {
        this.fieldName = fieldName;
    }
}

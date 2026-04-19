package pl.kaminski.powerscore.open_banking.application.contract;

import lombok.Data;

@Data
public class StartBankingReportRequest {
    private String sessionId;
    private String sessionIdSignature;
    private String ownerExternalId;
}

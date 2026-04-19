package pl.kaminski.powerscore.open_banking.query.contract;

import pl.kaminski.powerscore.open_banking.domain.OpenBankingStatus;

import java.util.Map;
import java.util.UUID;

public record OpenBankingReportInfo(
        UUID id,
        OpenBankingStatus status,
        Map<String, Object> reportData
) {
}

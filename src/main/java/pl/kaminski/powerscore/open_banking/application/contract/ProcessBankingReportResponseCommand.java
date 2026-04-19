package pl.kaminski.powerscore.open_banking.application.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kaminski.powerscore.commons.EntityId;

@Data
@AllArgsConstructor
public class ProcessBankingReportResponseCommand {
    private EntityId openBankingReportId;
}

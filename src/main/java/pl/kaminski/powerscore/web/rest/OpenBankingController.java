package pl.kaminski.powerscore.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.application.contract.IOpenBankingFacade;
import pl.kaminski.powerscore.open_banking.application.contract.StartBankingReportRequest;
import pl.kaminski.powerscore.open_banking.application.contract.StartBankingReportResult;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenBankingController {

    private final IOpenBankingFacade openBankingFacade;

    @PostMapping("/openBanking/{id}/startReport")
    public StartBankingReportResult startBankingReport(@PathVariable UUID id, @RequestBody StartBankingReportRequest request) {
        return openBankingFacade.startBankingReport(EntityId.from(id), request);
    }
}

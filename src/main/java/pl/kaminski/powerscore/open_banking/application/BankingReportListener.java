package pl.kaminski.powerscore.open_banking.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.kaminski.powerscore.open_banking.application.contract.ProcessBankingReportResponseCommand;
import pl.kaminski.powerscore.open_banking.application.contract.RequestBankingReportCommand;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingStatus;

import java.util.Map;

@RequiredArgsConstructor
public class BankingReportListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    private final IOpenBankingReportRepository openBankingReportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KontomatikClient kontomatikClient;

    @TransactionalEventListener(RequestBankingReportCommand.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    @SneakyThrows
    void requestBankingReport(RequestBankingReportCommand message) {

        var openBankingReport = openBankingReportRepository.findById(message.getOpenBankingReportId())
                .orElseThrow(() -> new IllegalStateException("No open banking report found for id: " + message.getOpenBankingReportId()));

        var commandId = openBankingReport.getCommandId();
        ObjectNode reportDataResult = kontomatikClient.getBankingReportData(commandId);
        boolean isFinished = reportDataResult.get("command").get("state").asText().equals("successful");
        while (!isFinished) {
            Thread.sleep(3_000);
            reportDataResult = kontomatikClient.getBankingReportData(commandId);
            isFinished = reportDataResult.get("command").get("state").asText().equals("successful");
        }

        ObjectNode aggregates = kontomatikClient.getBankingAggregates(openBankingReport.getOwnerExternalId());
        ObjectNode reportData = new ObjectNode(JsonNodeFactory.instance);
        reportData.set("aggregates", aggregates);
        reportData.set("accountInfo", formatAccountInfo(reportDataResult));
        var reportDataMap = OBJECT_MAPPER.convertValue(reportData, MAP_TYPE_REFERENCE);

        openBankingReport.setStatus(OpenBankingStatus.FINISHED);
        openBankingReport.setReportData(reportDataMap);
        openBankingReport.setTemporaryData("Banking report data");
        applicationEventPublisher.publishEvent(new ProcessBankingReportResponseCommand(openBankingReport.getId()));
    }

    private JsonNode formatAccountInfo(ObjectNode reportDataResult) {
        var result = new ObjectNode(JsonNodeFactory.instance);
        result.set("owners", reportDataResult.get("command").get("result").get("owners").get("owner"));
        var account = (ObjectNode) reportDataResult.get("command").get("result").get("accounts").get("account");
        account.remove("moneyTransactions");
        account.remove("owners");
        result.set("accounts", account);
        return result;
    }

}

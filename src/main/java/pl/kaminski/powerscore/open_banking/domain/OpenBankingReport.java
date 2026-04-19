package pl.kaminski.powerscore.open_banking.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.application.contract.StartBankingReportRequest;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "open_banking_reports")
@Data
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OpenBankingReport {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "open_banking_report_id"))
    private EntityId id;
    @Enumerated(EnumType.STRING)
    private OpenBankingStatus status;
    private String temporaryData;
    private String sessionId;
    private String sessionIdSignature;
    private String ownerExternalId;
    private String commandId;

    // zmiana na Json

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> reportData = new HashMap<>();

    public static OpenBankingReport create() {
        return OpenBankingReport.builder()
                .id(EntityId.newId())
                .status(OpenBankingStatus.PREPARED)
                .build();
    }

    public void startBankingReport(StartBankingReportRequest request, String commandId) {
        this.status = OpenBankingStatus.IN_PROGRESS;
        sessionId = request.getSessionId();
        sessionIdSignature = request.getSessionIdSignature();
        ownerExternalId = request.getOwnerExternalId();
        this.commandId = commandId;
    }
}

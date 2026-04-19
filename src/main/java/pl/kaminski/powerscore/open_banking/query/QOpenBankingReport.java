package pl.kaminski.powerscore.open_banking.query;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingStatus;
import pl.kaminski.powerscore.open_banking.query.contract.OpenBankingReportInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "open_banking_reports")
@Data
public class QOpenBankingReport {

    @Id
    @Column(name = "open_banking_report_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private OpenBankingStatus status;
    private String temporaryData;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> reportData = new HashMap<>();

    public OpenBankingReportInfo asDto() {
        return new OpenBankingReportInfo(id, status, reportData);
    }

}

package pl.kaminski.powerscore.verification.query.contract;

import lombok.Data;
import pl.kaminski.powerscore.commons.DateTimeRange;
import pl.kaminski.powerscore.commons.PaginationFilter;
import pl.kaminski.powerscore.commons.SortOrder;
import pl.kaminski.powerscore.verification.domain.VerificationStatus;

import java.util.List;

@Data
public class VerificationFilter {
    private VerificationSortBy sortBy;
    private SortOrder sortOrder;
    private PaginationFilter paginationFilter;
    private List<VerificationStatus> statuses;
    private DateTimeRange startDate;
    private DateTimeRange endDate;
    private ClientFilters clientFilters;
    private IdentityDocumentFilters identityDocumentFilters;
}

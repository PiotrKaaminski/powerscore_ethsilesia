package pl.kaminski.powerscore.verification.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.IVerificationQueryRepository;
import pl.kaminski.powerscore.verification.query.QIdentityDocumentImage;
import pl.kaminski.powerscore.client.query.QClient_;
import pl.kaminski.powerscore.verification.query.QIdentityDocument_;
import pl.kaminski.powerscore.verification.query.QVerification;
import pl.kaminski.powerscore.verification.query.QVerification_;
import pl.kaminski.powerscore.verification.query.contract.ClientFilters;
import pl.kaminski.powerscore.verification.query.contract.IdentityDocumentFilters;
import pl.kaminski.powerscore.verification.query.contract.VerificationFilter;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class VerificationQueryRepository implements IVerificationQueryRepository {

    private final VerificationQueryJpaRepository verificationJpaRepository;
    private final QIdentityDocumentImageJpaRepository identityDocumentImageJpaRepository;


    @Override
    public Optional<QVerification> findByTypeAndNumber(IdentityDocumentType type, String number) {
        return verificationJpaRepository.findByTypeAndNumber(type, number);
    }

    @Override
    public Optional<QVerification> findById(UUID id) {
        return verificationJpaRepository.findById(id);
    }

    @Override
    public PaginatedResponse<QVerification> getAllVerifications(VerificationFilter filters) {
        if (filters.getPaginationFilter() == null) filters.setPaginationFilter(new PaginationFilter());
        var pageRequest = PageRequest.of(
                filters.getPaginationFilter().getPage(),
                filters.getPaginationFilter().getPageSize(),
                filters.getSortOrder() == null ? Sort.Direction.DESC : Sort.Direction.valueOf(filters.getSortOrder().name()),
                filters.getSortBy() == null ? QVerification_.START_DATE : filters.getSortBy().getFieldName()
        );
        var specification = toSpecification(filters);

        var dbPage = verificationJpaRepository.findAll(specification, pageRequest);

        return new PaginatedResponse<>(
                dbPage.getContent(),
                dbPage.getTotalElements(),
                dbPage.getTotalPages(),
                filters.getPaginationFilter().getPage(),
                filters.getPaginationFilter().getPageSize()
        );
    }

    @Override
    public Optional<QIdentityDocumentImage> findImageById(UUID identityDocumentId) {
        return identityDocumentImageJpaRepository.findById(identityDocumentId);
    }
    private Specification<QVerification> toSpecification(VerificationFilter filters) {
        Specification<QVerification> specification = ((root, query, cb) -> cb.conjunction());
        if (filters.getStatuses() != null && !filters.getStatuses().isEmpty()) {
            specification = specification.and(((root, query, cb) ->
                    root.get(QVerification_.STATUS).in(filters.getStatuses()))
            );
        }
        if (filters.getClientFilters() != null) {
            specification = specification.and(clientSpecification(filters.getClientFilters()));
        }
        if (filters.getIdentityDocumentFilters() != null) {
            specification = specification.and(identityDocumentSpecification(filters.getIdentityDocumentFilters()));
        }
        if (filters.getStartDate() != null) {
            if (filters.getStartDate().getFrom() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get(QVerification_.START_DATE), filters.getStartDate().getFrom()))
                );
            }
            if (filters.getStartDate().getTo() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get(QVerification_.START_DATE), filters.getStartDate().getTo()))
                );
            }
        }
        if (filters.getEndDate() != null) {
            if (filters.getEndDate().getFrom() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get(QVerification_.FINISH_DATE), filters.getEndDate().getFrom()))
                );
            }
            if (filters.getEndDate().getTo() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get(QVerification_.FINISH_DATE), filters.getEndDate().getTo()))
                );
            }
        }
        return specification;
    }

    private Specification<QVerification> clientSpecification(ClientFilters filters) {
        Specification<QVerification> specification = ((root, query, cb) -> cb.conjunction());
        if (StringUtils.hasText(filters.getFirstName())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get(QVerification_.CLIENT).get(QClient_.FIRST_NAME)), filters.getFirstName().toLowerCase() + "%"))
            );
        }
        if (StringUtils.hasText(filters.getLastName())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get(QVerification_.CLIENT).get(QClient_.LAST_NAME)), filters.getLastName().toLowerCase() + "%"))
            );
        }
        if (filters.getBirthdate() != null) {
            if (filters.getBirthdate().getFrom() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get(QVerification_.CLIENT).get(QClient_.BIRTHDATE), filters.getBirthdate().getFrom()))
                );
            }
            if (filters.getBirthdate().getTo() != null) {
                specification = specification.and(((root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get(QVerification_.CLIENT).get(QClient_.BIRTHDATE), filters.getBirthdate().getTo()))
                );
            }
        }
        if (StringUtils.hasText(filters.getNationality())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get(QVerification_.CLIENT).get(QClient_.NATIONALITY)), filters.getNationality().toLowerCase() + "%"))
            );
        }
        if (StringUtils.hasText(filters.getEmail())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get(QVerification_.CLIENT).get(QClient_.EMAIL)), filters.getEmail().toLowerCase() + "%"))
            );
        }
        if (StringUtils.hasText(filters.getPhoneNumber())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(root.get(QVerification_.CLIENT).get(QClient_.PHONE_NUMBER), filters.getPhoneNumber() + "%"))
            );
        }
        return specification;
    }

    private Specification<QVerification> identityDocumentSpecification(IdentityDocumentFilters filters) {
        Specification<QVerification> specification = ((root, query, cb) -> cb.conjunction());
        if (filters.getIdentityDocumentTypes() != null && !filters.getIdentityDocumentTypes().isEmpty()) {
            specification = specification.and(((root, query, cb) ->
                    root.get(QVerification_.IDENTITY_DOCUMENT).get(QIdentityDocument_.TYPE).in(filters.getIdentityDocumentTypes()))
            );
        }
        if (StringUtils.hasText(filters.getNumber())) {
            specification = specification.and(((root, query, cb) ->
                    cb.like(cb.lower(root.get(QVerification_.IDENTITY_DOCUMENT).get(QIdentityDocument_.NUMBER)), filters.getNumber().toLowerCase() + "%"))
            );
        }
        return specification;
    }
}

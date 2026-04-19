package pl.kaminski.powerscore.open_banking.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import pl.kaminski.powerscore.open_banking.application.BankingReportListener;
import pl.kaminski.powerscore.open_banking.application.KontomatikClient;
import pl.kaminski.powerscore.open_banking.application.OpenBankingFacade;
import pl.kaminski.powerscore.open_banking.application.contract.IOpenBankingFacade;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;

public class OpenBankingConfiguration {

    private final IOpenBankingReportRepository openBankingReportRepository;

    public OpenBankingConfiguration(OpenBankingReportJpaRepository jpaRepository) {
        this.openBankingReportRepository = new OpenBankingReportRepository(jpaRepository);
    }

    @Bean
    KontomatikClient kontomatikClient(@Value("${kontomatic.api-key}") String apiKey,
                                      @Value("${kontomatic.data-since}") String dataSince) {
        return new KontomatikClient(apiKey, dataSince);
    }

    @Bean
    IOpenBankingFacade openBankingFacade(ApplicationEventPublisher applicationEventPublisher, KontomatikClient kontomatikClient) {
        return new OpenBankingFacade(openBankingReportRepository, applicationEventPublisher, kontomatikClient);
    }

    @Bean
    BankingReportListener bankingReportListener(ApplicationEventPublisher applicationEventPublisher, KontomatikClient kontomatikClient) {
        return new BankingReportListener(openBankingReportRepository, applicationEventPublisher, kontomatikClient);
    }

    @Bean
    IOpenBankingReportRepository openBankingReportRepository() {
        return openBankingReportRepository;
    }
}


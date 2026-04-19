package pl.kaminski.powerscore.final_decision.infrastructure;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import pl.kaminski.powerscore.final_decision.application.FinalDecisionFacade;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.final_decision.domain.IFinalDecisionRepository;

public class FinalDecisionConfiguration {

    private final IFinalDecisionRepository finalDecisionRepository;

    FinalDecisionConfiguration(FinalDecisionJpaRepository jpaRepository) {
        this.finalDecisionRepository = new FinalDecisionRepository(jpaRepository);
    }

    @Bean
    IFinalDecisionFacade agreementFacade(ApplicationEventPublisher applicationEventPublisher) {
        return new FinalDecisionFacade(finalDecisionRepository, applicationEventPublisher);
    }

}

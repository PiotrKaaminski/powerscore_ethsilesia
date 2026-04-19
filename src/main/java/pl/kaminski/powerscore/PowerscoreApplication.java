package pl.kaminski.powerscore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.kaminski.powerscore.final_decision.infrastructure.FinalDecisionConfiguration;
import pl.kaminski.powerscore.ai_recommendation.infrastructure.AiRecommendationConfiguration;
import pl.kaminski.powerscore.client.infrastructure.ClientConfiguration;
import pl.kaminski.powerscore.commons.CommonsConfiguration;
import pl.kaminski.powerscore.open_banking.application.KontomatikClient;
import pl.kaminski.powerscore.open_banking.infrastructure.OpenBankingConfiguration;
import pl.kaminski.powerscore.verification.infrastructure.VerificationConfiguration;

@SpringBootApplication
@Import({CommonsConfiguration.class, ClientConfiguration.class, VerificationConfiguration.class, OpenBankingConfiguration.class, AiRecommendationConfiguration.class, FinalDecisionConfiguration.class})
@EnableAsync
public class PowerscoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerscoreApplication.class, args);
    }

}

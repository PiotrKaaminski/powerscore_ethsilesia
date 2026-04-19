package pl.kaminski.powerscore.ai_recommendation.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.kaminski.powerscore.ai_recommendation.application.contract.ProcessAiRecommendationResponseCommand;
import pl.kaminski.powerscore.ai_recommendation.application.contract.RequestAiRecommendationCommand;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendation;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendationStatus;
import pl.kaminski.powerscore.ai_recommendation.domain.IAiRecommendationRepository;
import pl.kaminski.powerscore.client.domain.IClientRepository;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingReport;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class AiRecommendationListener {

    private static final SystemMessage SYSTEM_MESSAGE = new SystemMessage("""
    Jesteś analitykiem bankowym, który został poproszony przez firmę energetyczną o przeanalizowanie transakcji na koncie bankowym klienta, który zamierza podpisać umowę
    na dostawe prądu. Na podstawie otrzymanych informacji czyli:
    - dane klienta, które wprowadził w formularzu rejestracyjnym, oraz dane klienta z rachunku bankowego(będziesz musiał porównać czy się zgadzają, oraz czy wyglądają na prawdziwe)
    - informacje o długach
    - informacje o hipotece
    - informacje o wydatkach na hazard
    - ogólna informacja ile pieniędzy wpłynęło na konto, a ile zostało wydane
    Musisz ustalić: 
    - scoring klienta od 0 do 1000, który będzie opisywał w jakim stopniu dany klient będzie w stanie terminowo płacić za usługę. 
    Nawet jeśli zdarzy mu się nie zapłacić terminowo, to jak łatwo będzie ściągnąć z niego ewentualne długi. 
    - Wytłumaczenie, które uzasadni nadany scoring. (Max 20 zdań)
    - Typ umowy, który powinien zostać zaproponowany klientowi. Do wyboru są opcje umowy: normalna, oraz pre-paid
    Kieruj się głównie tym ile klient wydaje pieniędzy w stosunku do tego ile mu wpływa na konto.
    Krytycznym punktem niech będzie 80% wydawanych pieniędzy, ale pamiętaj, żeby zostawić margines na dodatkowe informacje, które dostaniesz.
    Informacje będą dostarczone w strukturze:
                      "label:salary": {
                        "all": {
                          "count": 3,
                          "average": 2800.00,
                          "total": 8400.00,
                          "numberOfDays": 3
                        },
                        "outgoing": {
                          "count": 0,
                          "average": null,
                          "total": 0.00,
                          "numberOfDays": 0
                        },
                        "incoming": {
                          "count": 3,
                          "average": 2800.00,
                          "total": 8400.00,
                          "numberOfDays": 3
                        }
                      }
                      
    więc masz nazwaną kategorie, informacje czy są to wpływy, czy wydatki, liczbę takich transakcji, oraz ich sumę. Pamiętaj aby je dokładnie przeanalizować.
    Pamiętaj też, że oceniasz jednego klienta, ale klientów może być kilkadziesiąt tysięcy, więc wyobraź sobie też różne inne scenariusze, które mógłbyś dostać,
    tak aby finalny scoring był faktycznym odzwierciedleniem ryzyka.
    Tą ocenę, oraz wybór umowy robisz dlatego, ponieważ w przytoczonej firmie energetycznej coraz częściej zdarza się, że klienci nie płacą za usługę,
    oraz ciężko ściągnąć zaległe płatności, stąd właśnie dostępny jest wybór umowy pre-paid, aby zabezpieczyć się przed takimi sytuacjami.
    
    """);

    /*
    scoring
    explanation
    AgreementType
     */
    private static final BeanOutputConverter<AiResponseModel> AI_RESPONSE_CONVERTER = new BeanOutputConverter<>(AiResponseModel.class);
    private static final String RESPONSE_MODEL_JSON_SCHEMA = AI_RESPONSE_CONVERTER.getJsonSchema();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> RESPONSE_MODEL_TYPE_REFERENCE = new TypeReference<>() {};

    private final IAiRecommendationRepository aiRecommendationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ChatModel chatModel;
    private final IClientRepository clientRepository;
    private final IOpenBankingReportRepository openBankingReportRepository;
    private final IVerificationRepository verificationRepository;


    @TransactionalEventListener(RequestAiRecommendationCommand.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    void requestAiRecommendation(RequestAiRecommendationCommand message) {

        var aiRecommendation = aiRecommendationRepository.findById(message.getAiRecommendationId())
                .orElseThrow(() -> new IllegalStateException("No ai recommendation found for id: " + message.getAiRecommendationId()));
        var aiMessage = constructMessage(message.getVerificationId());

        var chatResponse = chatModel.call(Prompt.builder()
                .messages(new UserMessage(aiMessage), SYSTEM_MESSAGE)
                .chatOptions(OpenAiChatOptions.builder()
                        .model("gpt-5.4-nano")
                        .outputSchema(RESPONSE_MODEL_JSON_SCHEMA).build())
                .build()
        );
        log.info("Ai requested, tokens used: {}", chatResponse.getMetadata().getUsage());
        var aiTextResponse = chatResponse.getResult().getOutput().getText();
        assert aiTextResponse != null : "AI response text cannot be null";
        log.info("AI response: {}", aiTextResponse);
        // z ai response trzeba będzie coś zrobić, klasę ai response rozszerzymy żeby byly tam wsyzstkie potrzebne pola
        var aiResponse = AI_RESPONSE_CONVERTER.convert(aiTextResponse);
        var aiResponseMap = OBJECT_MAPPER.convertValue(aiResponse, RESPONSE_MODEL_TYPE_REFERENCE);
        aiRecommendation.setStatus(AiRecommendationStatus.FINISHED);
        aiRecommendation.setRecommendationData(aiResponseMap);
//        aiRecommendation.setTemporaryData("AI Summary data");
        applicationEventPublisher.publishEvent(new ProcessAiRecommendationResponseCommand(aiRecommendation.getId()));
    }

    @SneakyThrows
    private String constructMessage(EntityId verificationId) {
        var verification = verificationRepository.findById(verificationId).get();
        var client = clientRepository.findById(verification.getClientId()).get();
        String openBankingReportString = "{}";
        if (verification.getOpenBankingReportId() != null) {
            var openBankingReport = openBankingReportRepository.findById(verification.getOpenBankingReportId()).get();
            openBankingReportString = OBJECT_MAPPER.writeValueAsString(openBankingReport.getReportData());
        }
        var clientJson = "{" +
                "\"firstName\": \"" + client.getFirstName().getFirstName() + "\"," +
                "\"lastName\": \"" + client.getLastName().getLastName() + "\"," +
                "\"email\": \"" + client.getEmail().getEmail() + "\"," +
                "\"phoneNumber\": \"" + client.getPhoneNumber().getPhoneNumber() + "\"," +
                "\"nationality\": \"" + client.getNationality().getNationality() + "\"," +
                "\"birthdate\": \"" + client.getBirthdate().toString() + "\"" +
                "}";
        return "clientJson: " + clientJson + ", openBankingReportJson: " + openBankingReportString;
    }

}

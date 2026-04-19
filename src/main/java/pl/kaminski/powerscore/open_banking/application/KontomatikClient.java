package pl.kaminski.powerscore.open_banking.application;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class KontomatikClient {

    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final List<String> AGGREGATE_CATEGORIES = List.of(
//            "label:compensation",
//            "label:invoice",
//            "label:salary",
//            "label:extra pay",
//            "label:500+",
            "label:bailiff|debt collection",
//            "label:repayment",
            "label:mortgage",
//            "label:repayment&bank",
//            "label:repayment~bank",
//            "label:credit card",
//            "label:loan",
//            "label:loan~bank",
//            "label:loan&bank",
            "label:gambling",
//            "label:repayment&late payment",
//            "label:verification",
//            "label:welfare",
//            "label:cash deposit",
//            "label:cash withdrawal",
//            "activeSinceAtLeast",
//            "balance",
            "allTransactions"
    );

    private final String apiKey;
    private final String dataSince;
    private final RestTemplate restTemplate;

    public KontomatikClient(String apiKey, String dataSince) {
        this.apiKey = apiKey;
        this.dataSince = dataSince;
        this.restTemplate = new RestTemplate();
    }

    @SneakyThrows
    public String defaultImport(String sessionId, String sessionIdSignature) {
        String url = "https://test.api.kontomatik.com/v1/command/default-import.xml";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-Api-Key", apiKey);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sessionId", sessionId);
        map.add("sessionIdSignature", sessionIdSignature);
        map.add("since", dataSince);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String response = restTemplate.postForObject(url, request, String.class);
        var responseJson = (ObjectNode) XML_MAPPER.readTree(response);
        return responseJson.get("command").get("id").asText();
    }

    @SneakyThrows
    public ObjectNode getBankingReportData(String commandId) {

        String url = "https://test.api.kontomatik.com/v1/command/" + commandId + ".xml";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        return (ObjectNode) XML_MAPPER.readTree(response);
    }

    public ObjectNode getBankingAggregates(String ownerExternalId) {
        var url = "https://test.api.kontomatik.com//v1/accounts-summary-with-monthly";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ownerExternalId", ownerExternalId);
        requestBody.put("start", dataSince);
        requestBody.put("end", LocalDate.now().toString());
        requestBody.put("categories", AGGREGATE_CATEGORIES);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        return (ObjectNode) restTemplate.postForObject(url, request, ObjectNode.class).get("content");
    }

    @SneakyThrows
    public ObjectNode getClientScoring(String ownerExternalId) {
        String url = "https://test.api.kontomatik.com/v1/owner-scores.xml?ownerExternalId=" + ownerExternalId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        return (ObjectNode) XML_MAPPER.readTree(response);
    }
}
package pl.kaminski.powerscore.ai_recommendation.application;

public record AiResponseModel(
        String scoring,
        String explanation,
        String agreementType) {
}

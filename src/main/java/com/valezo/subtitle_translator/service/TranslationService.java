package com.valezo.subtitle_translator.service;

import com.valezo.subtitle_translator.dto.GoogleTranslateResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    private final WebClient webClient;

    // constructor injection
    public TranslationService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://translation.googleapis.com").build();
    }

    public Mono<String> translateText(String text, String targetLanguage, String apiKey) {
        // Example body for Google Translate free-tier API
        // "q" param is the text, "target" is the language, "key" is your API key
        // Check official docs for exact payload structure
        Map<String, String> body = new HashMap<>();
        body.put("q", text);
        body.put("target", targetLanguage);
        body.put("format", "text");

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/language/translate/v2")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("q", text, "target", targetLanguage, "format", "text"))
                .retrieve()
                // We can transform the JSON into a custom model:
                .bodyToMono(GoogleTranslateResponse.class)
                .map(response -> {
                    // Grab the text from data.translations[0].translatedText
                    if (response.getData() != null && !response.getData().getTranslations().isEmpty()) {
                        return response.getData().getTranslations().get(0).getTranslatedText();
                    } else {
                        return ""; // fallback if something is missing
                    }
                });
    }
}

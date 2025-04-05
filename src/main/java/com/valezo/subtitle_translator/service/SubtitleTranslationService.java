package com.valezo.subtitle_translator.service;

import com.valezo.subtitle_translator.dto.SubtitleLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SubtitleTranslationService {

    private final SubtitleParserService parserService;
    private final TranslationService translationService;

    @Value("${translator.google.api-key}")
    private String googleApiKey;

    public SubtitleTranslationService(SubtitleParserService parserService,
                                      TranslationService translationService) {
        this.parserService = parserService;
        this.translationService = translationService;
    }

    public Mono<String> translateSrt(String srtContent, String targetLanguage) {
        // 1. Parse SRT into lines
        List<SubtitleLine> lines = parserService.parseSrt(srtContent);

        // 2. Translate lines in sequence (for simplicity)
        // If performance is needed, you can do parallel calls with Flux or chunk them.
        return Flux.fromIterable(lines)
                .flatMap(line -> translationService
                        .translateText(line.getText(), targetLanguage, googleApiKey)
                        .map(translated -> {
                            line.setText(translated);
                            return line;
                        }))
                .collectList()
                .map(translatedLines -> parserService.toSrt(translatedLines));
    }
}

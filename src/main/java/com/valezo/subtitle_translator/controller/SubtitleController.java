package com.valezo.subtitle_translator.controller;

import com.valezo.subtitle_translator.service.SubtitleTranslationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/subtitles")
public class SubtitleController {

    private final SubtitleTranslationService translationService;

    public SubtitleController(SubtitleTranslationService translationService) {
        this.translationService = translationService;
    }
    @PostMapping("/translate")
    public Mono<ResponseEntity<String>> translateSubtitle(
            @RequestPart("file") MultipartFile file,
            @RequestParam("targetLang") String targetLang
    ) {
        try {
            String srtContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            return translationService.translateSrt(srtContent, targetLang)
                    .map(translated -> {
                        // 1) Write the translated content to a file in src/main/resources/srt
                        //    This path is relative to your project root when running from IDE.

                        Path outputPath = Paths.get("src/main/resources/srt/translated.srt");
                        try {
                            Files.write(outputPath, translated.getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                            // You might want to handle the error or return a response indicating failure
                        }

                        // 2) Also return the file in the response as plain text
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.TEXT_PLAIN);
                        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=translated.srt");

                        return new ResponseEntity<>(translated, headers, HttpStatus.OK);
                    });
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

}

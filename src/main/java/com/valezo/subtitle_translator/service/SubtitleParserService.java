package com.valezo.subtitle_translator.service;

import com.valezo.subtitle_translator.dto.SubtitleLine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubtitleParserService {

    public List<SubtitleLine> parseSrt(String srtContent) {
        // Very simplistic approach: split by blank lines.
        // A better approach might use a library to handle edge cases.
        List<SubtitleLine> lines = new ArrayList<>();
        String[] blocks = srtContent.split("\\r?\\n\\r?\\n");
        for (String block : blocks) {
            // Each block typically:
            // 1) Index line
            // 2) Timecode line
            // 3) Subtitle text (could be multiple lines)
            String[] blockLines = block.split("\\r?\\n");
            if (blockLines.length < 3) {
                continue;
            }
            try {
                int index = Integer.parseInt(blockLines[0].trim());
                String timecode = blockLines[1].trim();
                // e.g. "00:00:01,000 --> 00:00:04,000"
                StringBuilder textBuilder = new StringBuilder();
                for (int i = 2; i < blockLines.length; i++) {
                    textBuilder.append(blockLines[i]).append("\n");
                }
                String subtitleText = textBuilder.toString().trim();

                lines.add(new SubtitleLine(index, timecode, subtitleText));
            } catch (NumberFormatException e) {
                // ignoring parse error
            }
        }
        return lines;
    }

    public String toSrt(List<SubtitleLine> lines) {
        // Rebuild a valid .srt string
        StringBuilder sb = new StringBuilder();
        for (SubtitleLine line : lines) {
            sb.append(line.getIndex()).append("\n")
                    .append(line.getTimecode()).append("\n")
                    .append(line.getText()).append("\n\n");
        }
        return sb.toString().trim();
    }
}

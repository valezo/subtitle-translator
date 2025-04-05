package com.valezo.subtitle_translator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubtitleLine {
    private int index;
    private String timecode;
    private String text;


    // constructor, getters, setters
    // ...
}

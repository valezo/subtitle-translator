# Subtitle Translator

An open-source Spring Boot service that automatically translates .srt subtitle files into a specified language using Google Translate. The core aim is to help viewers who need subtitles in their native language but only have access to subtitles in another language.

## Vision / Future Potential

Our long-term vision is that streaming services (Netflix, HBO, Amazon Prime, Disney+, etc.) might integrate this kind of technology to automatically generate or supplement subtitles in every possible language. That means families or groups who speak less-common languages would no longer feel left out or forced to watch content only in major languages. By partnering with these platforms, an on-demand subtitle translation system could:

- Leverage official subtitle files that streaming services already possess.
- Dynamically generate translations for niche languages not officially supported.
- Provide immediate global accessibility—letting more viewers watch together in their home language.
- Possibly refine and improve translations using user feedback or partial human editing.

If big streaming providers adopt a pipeline like this, it could expand their markets and user satisfaction, since many more people around the world would have localized subtitles available.

## Features

1. **File Upload:**
   Accepts .srt subtitle files via a REST endpoint.

2. **Translation:**
   Integrates with the Google Translate API. Could be extended to other providers (DeepL, GPT-based translation, etc.).

3. **Preserve Timecodes:**
   Keeps the original .srt format and timecodes intact, only replacing the text with translated content.

4. **Output:**
   Returns a newly translated .srt file as plain text in the HTTP response, and optionally saves it locally.

## Prerequisites

- Java 17+ (or any version supported by Spring Boot).
- Maven or Gradle (depending on your build tool).
- A Google Cloud Project with the Cloud Translation API enabled, and a valid API key. Make sure billing is enabled, and the key is authorized for your environment.

## Installation

1. **Clone the Repository:**
   ```
   git clone https://github.com/your-username/subtitle-translator.git
   cd subtitle-translator
   ```

2. **Configure the API Key:**
   In application.properties (or via environment variable), set:
   ```
   translator.google.api-key=YOUR_REAL_GCLOUD_KEY
   ```

3. **Build & Run:**
   - Using Maven:
     ```
     mvn spring-boot:run
     ```

   The service starts at http://localhost:8080

## Usage

1. **POST /api/subtitles/translate**

   Multipart form fields:
   - `file=@/path/to/your.srt`
   - `targetLang=es` (for example)

   Example cURL:
   ```
   curl -X POST \
   -F "file=@src/main/resources/srt/example.srt" \
   -F "targetLang=de" \
   http://localhost:8080/api/subtitles/translate \
   --output translated.srt
   ```

   This downloads translated.srt with the new text.

2. **Output Files:**

   By default, you get the translated text in the HTTP response. It will also save the file under src/main/resources/srt/translated.srt (depending on the controller logic).

## Customization

- You can swap in different translation APIs by modifying the TranslationService.
- SubtitleParserService is a basic parser for .srt. If advanced features are needed, use a robust library.
- If dealing with large files or many lines, consider concurrency with Reactor (Flux) for parallel requests.

## Motivation

We built this to allow families or groups with less-common native languages to watch content together. Official subtitles might not always exist in these languages, so auto-translation can bridge that gap. It's not perfect, but it's often good enough to enjoy the story together.

## Roadmap

- Batch / Bulk Translation (translate multiple .srt files at once).
- Web UI for uploading .srt, picking a language, and downloading new .srt.
- Error Handling & Logging: better resilience with large or corrupt files.
- Refactoring

## Contributing

Contributions, feature ideas, and bug reports are welcome! Submit pull requests or open issues to improve and expand the tool.

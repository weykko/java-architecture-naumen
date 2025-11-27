package ru.urfu.exporter;

import org.springframework.stereotype.Component;
import ru.urfu.document.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Экспортёр документов в формат TXT.
 */
@Component
public class TxtExporter implements DocumentExporter {

    @Override
    public void export(Document document, Path outputPath) throws IOException {
        Files.writeString(outputPath, document.content());
    }

    @Override
    public String getFormat() {
        return "txt";
    }
}


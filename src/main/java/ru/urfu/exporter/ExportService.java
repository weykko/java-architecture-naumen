package ru.urfu.exporter;

import org.springframework.stereotype.Service;
import ru.urfu.document.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для экспорта документов.
 * Управляет доступными экспортёрами и выполняет экспорт в нужном формате.
 */
@Service
public class ExportService {

    private final Map<String, DocumentExporter> exporters;
    private static final Path OUTPUT_DIR =
            Path.of(System.getProperty("user.home"), "lessonSOLID");

    /**
     * Конструктор с инжекцией всех доступных экспортёров.
     *
     * @param exporterList список всех экспортёров, зарегистрированных в Spring контексте
     */
    public ExportService(List<DocumentExporter> exporterList) {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(
                        DocumentExporter::getFormat,
                        Function.identity()
                ));
    }

    /**
     * Экспортирует документ в указанном формате.
     *
     * @param document документ для экспорта
     * @param format формат экспорта
     * @return путь к созданному файлу
     * @throws IOException если произошла ошибка при экспорте
     * @throws IllegalArgumentException если формат не поддерживается
     */
    public Path export(Document document, String format) throws IOException {
        DocumentExporter exporter = getExporter(format)
                .orElseThrow(() -> new IllegalArgumentException("Неподдерживаемый формат: " + format));

        Files.createDirectories(OUTPUT_DIR);

        Path outputPath = OUTPUT_DIR.resolve(document.name() + "." + format);
        exporter.export(document, outputPath);

        return outputPath;
    }

    /**
     * Возвращает экспортёр по формату.
     *
     * @param format формат экспорта
     * @return Optional с экспортёром или пустой Optional, если формат не поддерживается
     */
    private Optional<DocumentExporter> getExporter(String format) {
        return Optional.ofNullable(exporters.get(format.toLowerCase()));
    }

    /**
     * Возвращает список поддерживаемых форматов.
     *
     * @return список форматов
     */
    public List<String> getSupportedFormats() {
        return List.copyOf(exporters.keySet());
    }
}


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
    private final Path outputDirectory;

    /**
     * Конструктор с инжекцией всех доступных экспортёров.
     *
     * @param exporterList список всех экспортёров, зарегистрированных в Spring контексте
     * @param outputDirectory директория для сохранения экспортированных файлов
     */
    public ExportService(List<DocumentExporter> exporterList, Path outputDirectory) {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(
                        DocumentExporter::getFormat,
                        Function.identity()
                ));
        this.outputDirectory = outputDirectory;
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

        Files.createDirectories(outputDirectory);

        Path outputPath = outputDirectory.resolve(document.name() + "." + format);
        exporter.export(document, outputPath);

        return outputPath;
    }

    /**
     * Возвращает экспортёр по формату.
     *
     * @param format формат экспорта
     * @return Optional с экспортёром или пустой Optional, если формат не поддерживается
     */
    public Optional<DocumentExporter> getExporter(String format) {
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


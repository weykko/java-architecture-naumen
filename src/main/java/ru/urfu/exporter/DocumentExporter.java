package ru.urfu.exporter;

import ru.urfu.document.Document;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Интерфейс для экспортёров документов.
 * Реализации этого интерфейса предоставляют различные форматы экспорта.
 */
public interface DocumentExporter {

    /**
     * Экспортирует документ в файл.
     *
     * @param document документ для экспорта
     * @param outputPath путь для сохранения файла
     * @throws IOException если произошла ошибка при экспорте
     */
    void export(Document document, Path outputPath) throws IOException;

    /**
     * Возвращает формат экспорта (расширение файла без точки).
     *
     * @return формат файла (например, "txt", "pdf", "docx")
     */
    String getFormat();
}


package ru.urfu.command;

import org.springframework.stereotype.Component;
import ru.urfu.document.Document;
import ru.urfu.document.DocumentService;
import ru.urfu.exporter.ExportService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Обработчик команд пользователя.
 * Реализует SRP - единственная ответственность: обработка команд и взаимодействие с пользователем.
 */
@Component
public class CommandHandler {

    private final DocumentService documentService;
    private final ExportService exportService;
    private final Scanner scanner;

    public CommandHandler(DocumentService documentService, ExportService exportService) {
        this.documentService = documentService;
        this.exportService = exportService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Обрабатывает команду создания документа.
     */
    public void handleCreate() {
        System.out.print("Введите имя документа: ");
        String name = scanner.nextLine().trim();

        System.out.println("Введите содержимое документа (пустая строка — завершить ввод):");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            content.append(line).append(System.lineSeparator());
        }

        documentService.createDocument(name, content.toString());
        System.out.println("Документ создан и сохранён в памяти.");
    }

    /**
     * Обрабатывает команду импорта документа.
     */
    public void handleImport() {
        System.out.print("Введите путь к txt файлу: ");
        String path = scanner.nextLine().trim();

        try {
            documentService.importTxt(path);
            System.out.println("Документ успешно импортирован.");
        } catch (IOException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает команду вывода списка документов.
     */
    public void handleList() {
        List<Document> documents = documentService.list();
        if (documents.isEmpty()) {
            System.out.println("Документов нет");
            return;
        }
        for (int i = 0; i < documents.size(); i++) {
            System.out.println(i + ": " + documents.get(i).name());
        }
    }

    /**
     * Обрабатывает команду экспорта документа.
     */
    public void handleExport() {
        System.out.print("Введите номер документа: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim());

            Optional<Document> documentOptional = documentService.getDocument(index);
            if (documentOptional.isEmpty()) {
                System.out.println("Нет документа с таким номером.");
                return;
            }

            Document document = documentOptional.get();

            List<String> supportedFormats = exportService.getSupportedFormats();
            System.out.print("Введите формат (" + String.join("/", supportedFormats) + "): ");
            String format = scanner.nextLine().trim().toLowerCase();

            Path outputPath = exportService.export(document, format);
            System.out.println("Экспорт выполнен: " + outputPath);

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: Введено некорректное число.");
        } catch (IllegalArgumentException e) {
            List<String> supportedFormats = exportService.getSupportedFormats();
            System.err.println("Ошибка: Неверный формат. Доступные форматы: " + String.join(", ", supportedFormats));
        } catch (IOException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}


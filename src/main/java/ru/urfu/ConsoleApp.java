package ru.urfu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.urfu.command.CommandHandler;

import java.util.Scanner;

/**
 * Основной класс консольного приложения.
 * Запускает цикл обработки команд пользователя.
 */
@SpringBootApplication
public class ConsoleApp implements CommandLineRunner {

    private final CommandHandler commandHandler;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Точка входа приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApp.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Консольное приложение ===");

        while (true) {
            System.out.println("\nКоманды: import, list, create, export, exit");
            System.out.print("> ");
            String cmd = scanner.nextLine().trim();

            switch (cmd) {
                case "create" -> commandHandler.handleCreate();
                case "import" -> commandHandler.handleImport();
                case "list" -> commandHandler.handleList();
                case "export" -> commandHandler.handleExport();
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Неизвестная команда");
            }
        }
    }
}


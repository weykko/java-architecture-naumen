package ru.urfu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

/**
 * Конфигурация приложения.
 */
@Configuration
public class AppConfig {

    /**
     * Создаёт бин для директории экспорта.
     *
     * @return путь к директории для экспортированных файлов
     */
    @Bean
    public Path outputDirectory() {
        return Path.of(System.getProperty("user.home"), "lessonSOLID");
    }
}


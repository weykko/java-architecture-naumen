package ru.urfu;

/**
 * Обработчик сообщений
 */
public class MessageHandler {

    /**
     * Делает обработку и возвращает обработанное сообщение
     */
    public String handleMessage(String message) {
        return "Ваше сообщение: '%s'".formatted(message);
    }
}

package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Тестовая реализация интерфейса {@link Bot}.
 * Хранит список отправленных сообщений.
 */
public class TestBot implements Bot {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    /**
     * Получить отправленное сообщение по id
     */
    public String getMessageById(int id) {
        return messages.get(id);
    }

}
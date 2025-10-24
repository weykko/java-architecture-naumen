import org.junit.Test;
import ru.urfu.MessageHandler;

import static org.junit.Assert.assertEquals;

/**
 * Класс тестирования MessageHandler
 */
public class MessageHandlerTest {

    MessageHandler messageHandler = new MessageHandler();

    /**
     * Стандартный тест работы метода обработки сообщения
     */
    @Test
    public void messageTest() {
        assertEquals("Ваше сообщение: 'привет'", messageHandler.handleMessage("привет"));
    }

    /**
     * Тест работы метода c пустым сообщением
     */
    @Test
    public void messageTestEmpty() {
        assertEquals("Ваше сообщение: ''", messageHandler.handleMessage(""));
    }
}

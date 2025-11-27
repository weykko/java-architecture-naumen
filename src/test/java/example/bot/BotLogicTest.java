package example.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса {@link BotLogic}
 * Проверяются команды /test, /notify и /repeat
 */
public class BotLogicTest {

    /**
     * Создание инстанса {@link TestBot}. 
     * Хранит список отправленных сообщений для проверки.
     */
    private final TestBot testBot = new TestBot();

    /**
     * Создание инстанса {@link BotLogic}.
     */
    private final BotLogic botLogic = new BotLogic(testBot);

    /**
     * Создание инстанса {@link User}.
     */
    private final User user = new User(525L);

    /**
     * Тест /test с правильными ответами
     */
    @Test
    public void testWithCorrectAnswersShouldSayThatRight() {
        botLogic.processCommand(user, "/test");
        assertEquals("Вычислите степень: 10^2", testBot.getMessageById(0));

        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", testBot.getMessageById(1));
        assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessageById(2));

        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", testBot.getMessageById(3));
    }

    /**
     * Тест команды /test с неправильными ответами
     */
    @Test
    public void testWithIncorrectAnswersShouldSayThatWrong() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "133");
        assertEquals("Вы ошиблись, верный ответ: 100", testBot.getMessageById(1));

        botLogic.processCommand(user, "133");
        assertEquals("Вы ошиблись, верный ответ: 6", testBot.getMessageById(3));
    }

    /**
     * Тест команды /notify с задержкой в 1 секунду
     */
    @Test
    public void notifyWithDelayOneSecondShouldNotifyMe() throws InterruptedException {
        botLogic.processCommand(user, "/notify");

        assertEquals("Введите текст напоминания", testBot.getMessageById(0));
        botLogic.processCommand(user, "notify me");

        assertEquals("Через сколько секунд напомнить?", testBot.getMessageById(1));
        botLogic.processCommand(user, "1");
        assertEquals("Напоминание установлено", testBot.getMessageById(2));

        Thread.sleep(975);
        assertEquals("Напоминание установлено", testBot.getLastMessage());
        Thread.sleep(50);

        assertEquals("Сработало напоминание: 'notify me'", testBot.getMessageById(3));
    }

    /**
     * Тест команды /notify с неверным форматом задержки
     */
    @Test
    public void notifyWithWrongFormatDelayShouldSayWarning() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "notify me");
        botLogic.processCommand(user, "not a number");

        assertEquals("Пожалуйста, введите целое число", testBot.getMessageById(2));
    }

    /**
     * Тест команды /notify с отрицательной задержкой
     */
    @Test
    public void notifyWithNegativeDelayShouldThrowException() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "notify me");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> botLogic.processCommand(user, "-133"));

        assertEquals("Negative delay.", exception.getMessage());
    }

    /**
     * Тест команды /repeat без неправильных ответов
     */
    @Test
    public void repeatWithNoWrongAnswersShouldEmptyRepeat() {
        botLogic.processCommand(user, "/repeat");

        assertEquals("Нет вопросов для повторения", testBot.getMessageById(0));
    }

    /**
     * Тест команды /repeat с одним неправильным ответом
     * Вопрос добавляется в повторение, если на него был дан неправильный ответ
     */
    @Test
    public void repeatWithOneWrongAnswersShouldOneBeInRepeat() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "mistake");

        botLogic.processCommand(user, "/repeat");
        assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessageById(5));
    }

    /**
     * Тест команды /repeat с неправильными ответами
     * Вопросы удаляются из повторения после правильных ответов
     */
    @Test
    public void repeatWithWrongAnswersShouldRemoveFromRepeat() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "mistake");
        botLogic.processCommand(user, "mistake");

        botLogic.processCommand(user, "/repeat");
        assertEquals("Вычислите степень: 10^2", testBot.getMessageById(5));
        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", testBot.getMessageById(6));

        assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessageById(7));
        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", testBot.getMessageById(8));

        botLogic.processCommand(user, "/repeat");
        assertEquals("Нет вопросов для повторения", testBot.getMessageById(10));
    }

    /**
     * Тест команды /repeat с неправильными ответами
     * Все вопросы остаются в повторении после повторных неправильных ответов
     */
    @Test
    public void repeatWithWrongAnswersShouldKeepInRepeat(){
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "mistake");
        botLogic.processCommand(user, "mistake");

        botLogic.processCommand(user, "/repeat");
        botLogic.processCommand(user, "mistake");
        assertEquals("Вы ошиблись, верный ответ: 100", testBot.getMessageById(6));

        botLogic.processCommand(user, "mistake");
        assertEquals("Вы ошиблись, верный ответ: 6", testBot.getMessageById(8));

        botLogic.processCommand(user, "/repeat");
        assertEquals("Вычислите степень: 10^2", testBot.getMessageById(10));
        botLogic.processCommand(user, "100");
        assertEquals("Сколько будет 2 + 2 * 2", testBot.getMessageById(12));
    }
}

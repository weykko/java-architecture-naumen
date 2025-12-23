package example.container;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса {@link Container}
 */
public class ContainerTest {

    private Container container;

    /**
     * Создание нового инстанса {@link Container} перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        container = new Container();
    }

    /**
     * Тестируем добавление элемента
     */
    @Test
    void addNewItemShouldItemBeAdded() {
        Item item = new Item(133);
        container.add(item);
        assertTrue(container.contains(item));
        assertEquals(1, container.size());
    }

    /**
     * Тестируем добавление элементов на коректные индексы
     */
    @Test
    void addNewItemsShouldItemBeAddedOnCorrectIndex() {
        Item item = new Item(133);
        Item item2 = new Item(134);
        container.add(item);
        container.add(item2);
        assertEquals(2, container.size());
        assertEquals(item, container.get(0));
        assertEquals(item2, container.get(1));
    }

    /**
     * Тестируем удаление существующего элемента
     */
    @Test
    void removeExistingItemShouldItemBeDeleted() {
        Item item = new Item(133);
        container.add(item);
        container.remove(item);
        assertFalse(container.contains(item));
        assertEquals(0, container.size());
    }

    /**
     * Тестируем удаление несуществующего элемента
     */
    @Test
    void removeNonExistingItemShouldReturnFalse() {
        Item item = new Item(133);
        Item item2 = new Item(134);
        container.add(item);
        boolean result = container.remove(item2);
        assertFalse(result);
        assertEquals(1, container.size());
    }
}

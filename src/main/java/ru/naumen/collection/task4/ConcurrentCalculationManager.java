package ru.naumen.collection.task4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

/**
 * Класс управления расчётами
 */
public class ConcurrentCalculationManager<T> {
    // Выбор коллекции: LinkedBlockingQueue для CompletableFuture
    // Почему именно LinkedBlockingQueue:
    // - Гарантирует FIFO (первым пришел - первым ушел), что обеспечивает строгий порядок результатов
    // - take() блокирует поток пока элемент не доступен, что идеально для ожидания результатов
    // - put() не блокируется (если очередь не ограничена), что позволяет быстро добавлять задачи
    //
    // Сложность алгоритма:
    // - addTask(): O(1) - добавление в конец очереди
    // - getResult(): O(1) - извлечение из начала очереди, но блокируется пока результат не готов
    //
    // Обоснование почему сложность именно такая:
    // - LinkedBlockingQueue использует связанный список, операции добавления/удаления O(1)
    // - CompletableFuture.get() блокируется до завершения вычисления, но само извлечение из очереди O(1)

    private final BlockingQueue<CompletableFuture<T>> resultQueue = new LinkedBlockingQueue<>();

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        // CompletableFuture.supplyAsync выполняет задачу асинхронно
        // Это позволяет выполнять несколько задач одновременно
        CompletableFuture<T> future = CompletableFuture.supplyAsync(task);

        // Добавляем future в очередь в порядке поступления задач
        // LinkedBlockingQueue.put() гарантирует thread-safe добавление
        try {
            resultQueue.put(future);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() {
        // take() блокируется пока в очереди не появится элемент
        // Затем извлекает CompletableFuture и вызывает get() который блокируется пока вычисление не завершится
        // Это обеспечивает строгий порядок: первая добавленная задача - первый результат
        try {
            CompletableFuture<T> future = resultQueue.take();
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
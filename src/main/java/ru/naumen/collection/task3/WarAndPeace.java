package ru.naumen.collection.task3;

import java.nio.file.Path;
import java.util.*;

/**
 * <p>Написать консольное приложение, которое принимает на вход произвольный текстовый файл в формате txt.
 * Нужно собрать все встречающийся слова и посчитать для каждого из них количество раз, сколько слово встретилось.
 * Морфологию не учитываем.</p>
 * <p>Вывести на экран наиболее используемые (TOP) 10 слов и наименее используемые (LAST) 10 слов</p>
 * <p>Проверить работу на романе Льва Толстого “Война и мир”</p>
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class WarAndPeace {

    private static final Path WAR_AND_PEACE_FILE_PATH = Path.of("src/main/resources",
            "Лев_Толстой_Война_и_мир_Том_1,_2,_3,_4_(UTF-8).txt");

    public static void main(String[] args) {
        // Выбор коллекций:
        // 1. LinkedHashMap для подсчета частоты слов
        //    Почему LinkedHashMap:
        //    - Быстрый доступ O(1) для обновления счетчиков
        //    - Нужно часто обновлять значения по ключу (слову)
        //    - Сложность: O(n), где n = количество всех слов
        //    - Память: O(m), где m - количество уникальных слов
        //    - Быстрая итерация между элементами
        //
        // 2. PriorityQueue для получения TOP 10 и LAST 10
        //    Почему PriorityQueue:
        //    - Эффективное получение min/max элементов
        //    - Для TOP 10 используем min-heap (чтобы быстро удалять наименьший из топовых)
        //    - Для LAST 10 используем max-heap (чтобы быстро удалять наибольший из последних)
        //    - Сложность: O(m log k) где k = 10 (фиксированный размер кучи), m - количество уникальных слов
        //    - По итогу из-за фиксированной кучи достигается линейная сложность O(m)
        //    - Память: O(k) - очень эффективно
        //
        // Сложность алгоритма:
        // - O(n + m), где n - количество всех слов, m - количество уникальных слов

        Map<String, Integer> freqMap = new LinkedHashMap<>();

        // Первый проход: подсчет частоты слов
        // Сложность: O(n) где n - общее количество слов в файле
        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> {
                    freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
                });

        // Используем PriorityQueue для поиска топ 10 популярных и редких слов
        // Сложность: O(m) где m - количество уникальных слов
        // Для популярных слов: минимальная куча по частоте
        PriorityQueue<Map.Entry<String, Integer>> top10Queue =
                new PriorityQueue<>(10, Comparator.comparingInt(Map.Entry::getValue));

        // Для редких слов: максимальная куча по частоте
        PriorityQueue<Map.Entry<String, Integer>> least10Queue =
                new PriorityQueue<>(10, (a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Заполняем очереди
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            top10Queue.offer(entry);
            least10Queue.offer(entry);

            if (top10Queue.size() > 10) {
                top10Queue.poll();
            }
            if (least10Queue.size() > 10) {
                least10Queue.poll();
            }
        }

        // Используем LinkedList для сборки сортированого в нужном порядке списка
        // Операция вставки в начало за O(1)
        System.out.println("\nТоп 10 самых частых слов:");
        printResult(top10Queue);

        System.out.println("\nТоп 10 самых редких слов:");
        printResult(least10Queue);
    }

    private static void printResult(PriorityQueue<Map.Entry<String, Integer>> queue) {
        List<Map.Entry<String, Integer>> result = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            result.addFirst(queue.poll());
        }

        result.forEach(kv -> System.out.println(kv.getKey() + ": " + kv.getValue()));
    }
}

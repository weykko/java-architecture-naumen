package ru.naumen.collection.task2;

import java.util.*;

/**
 * Дано:
 * <pre>
 * public class User {
 *     private String username;
 *     private String email;
 *     private byte[] passwordHash;
 *     …
 * }
 * </pre>
 * Нужно реализовать метод
 * <pre>
 * public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB);
 * </pre>
 * <p>который возвращает дубликаты пользователей, которые есть в обеих коллекциях.</p>
 * <p>Одинаковыми считаем пользователей, у которых совпадают все 3 поля: username,
 * email, passwordHash. Дубликаты внутри коллекций collA, collB можно не учитывать.</p>
 * <p>Метод должен быть оптимален по производительности.</p>
 * <p>Пользоваться можно только стандартными классами Java SE.
 * Коллекции collA, collB изменять запрещено.</p>
 *
 * См. {@link User}
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Task2
{

    /**
     * Возвращает дубликаты пользователей, которые есть в обеих коллекциях
     */
    public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB) {
        // Выбор коллекции: HashSet
        // Почему именно HashSet:
        // - Нужна быстрая проверка наличия элемента - O(1) в среднем случае
        // - Пользователи должны иметь правильные equals() и hashCode() для корректной работы
        // - HashSet автоматически удаляет дубликаты, что соответствует условию "дубликаты внутри коллекций можно не учитывать"
        //
        // Сложность алгоритма:
        // - O(n + m), где n = размер collA, m = размер collB
        //
        // Обоснование почему сложность именно такая:
        // - Создание HashSet из collA: O(n), если нет коллизий, они минимизируются за счет хорошей хешфункци
        // - Проход по collB и проверка наличия в HashSet: O(m)

        // Оптимизация: выбираем меньшую коллекцию для HashSet чтобы сэкономить память
        Collection<User> smallerCollection = collA.size() <= collB.size() ? collA : collB;
        Collection<User> largerCollection = collA.size() <= collB.size() ? collB : collA;

        // Создаем HashSet из меньшей коллекции для экономии памяти
        // HashSet обеспечивает O(1) для contains()
        Set<User> userSet = new HashSet<>(smallerCollection);

        // Создаем List с размером меньшей коллекции, чтобы его не расширять
        List<User> duplicates = new ArrayList<>(smallerCollection.size());

        // Проходим по большей коллекции и ищем пересечения
        for (var user : largerCollection) {
            if (userSet.contains(user)) {
                duplicates.add(user);
            }
        }

        return duplicates;
    }
}

package ru.naumen.collection.task2;

import java.util.Arrays;
import java.util.Objects;

/**
 * Пользователь
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class User {
    private String username;
    private String email;
    private byte[] passwordHash;

    // Для HashMap важно переопределить equals и hashcode.
    // По условию задачи считаем пользователей равными при совпадении всех трёх полей,
    // поэтому equals и hashcode определяется по всем полям

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;

        return Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Arrays.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, Arrays.hashCode(passwordHash));
    }
}

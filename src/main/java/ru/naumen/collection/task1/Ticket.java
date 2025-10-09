package ru.naumen.collection.task1;

import java.util.Objects;

/**
 * Билет
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Ticket {
    private long id;
    private String client;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ticket ticket)) return false;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package ru.oop.task3;

/**
 * Транспортное средство
 */
public interface Vehicle extends Positioned {
    /**
     * Подьезд к ближайшему доступному месту
     */
    public void rideToNearest(Position destination);

    /**
     * Сесть человеку в транспорт
     */
    public void getInto(Person person);

    /**
     * Выйти человеку из транспорта
     */
    public void getOut(Person person);
}

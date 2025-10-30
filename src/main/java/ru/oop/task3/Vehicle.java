package ru.oop.task3;

/**
 * Класс транспортного средства, использующийся для передвижения людей
 */
public interface Vehicle extends Positioned {
    /**
     * Подьезд к ближайшему доступному месту
     */
    public void rideToNearest(Person person, Position destination);

}

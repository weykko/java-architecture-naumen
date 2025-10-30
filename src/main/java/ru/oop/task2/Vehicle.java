package ru.oop.task2;

/**
 * Класс транспортного средства, использующийся для передвижения людей
 */
public interface Vehicle extends Positioned{
    /**
     * Подьезд к ближайшему доступному месту, с указаным человек
     */
    public void rideToNearest(Person person, Position destination);

}

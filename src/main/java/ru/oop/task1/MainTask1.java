package ru.oop.task1;

/**
 * Задача: нужно добраться человеку на машине до заданного места.<br>
 * Но не всегда можно подъехать прямо к нужному месту, может понадобиться дойти пешком.<br>
 * Человек знает своё текущее местоположение (у него есть метод {@link Person#getPosition()}<br>
 * <p>
 * <ul>
 *   <li>Код не должен превышать 6 строк</li>
 *   <li>Запрещено реализовывать конструкторы и методы, кроме {@link #moveTo(Person, Position)}</li>
 *   <li>Запрещено добавлять новые методы в класс Main</li>
 *   <li>Разрешено создавать новые классы и интерфейсы</li>
 *   <li>Все добавленные интерфейсы, классы и методы должны иметь JavaDoc!</li>
 * </ul>
 *
 * @author vpyzhyanov
 * @since 21.10.2020
 */
public class MainTask1 {

    /**
     * Переехать из текущего места в заданную точку
     */
    public void moveTo(Person person, Position destination) {
        var car = new Car(person);
        car.rideToNearest(destination);
        person.walk(destination);
        assert person.getPosition() == destination;
    }
}

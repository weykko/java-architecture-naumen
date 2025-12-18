package shopping;

import customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import product.Product;
import product.ProductDao;

/**
 * Тестирование класса {@link ShoppingService}
 */
@ExtendWith(MockitoExtension.class)
public class ShoppingServiceTest {

    /**
     * Общий покупатель для всех тестов
     */
    private static Customer customer;

    /**
     * Корзина для каждого теста
     */
    private Cart cart;

    /**
     * Мок для ProductDao
     */
    private final ProductDao productDaoMock;

    /**
     * Тестируемый сервис
     */
    private final ShoppingService shoppingService;

    /**
     * Конструктор с внедрением моков
     */
    public ShoppingServiceTest(@Mock ProductDao productDaoMock) {
        this.productDaoMock = productDaoMock;
        this.shoppingService = new ShoppingServiceImpl(productDaoMock);
    }

    /**
     * Создание инстансов, общих для всех тестов.
     * Так как Customer просто передается и нигде не успользуется (это логическая ошибка).
     */
    @BeforeAll
    public static void setUpClass() {
        customer = new Customer(1L, "+79222222222");
    }

    /**
     * Создание инстансов, перед каждым тестом.
     * Корзина изменяемый объект, поэтому создаём новый перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        this.cart = new Cart(customer);
    }

    /**
     * Этот метод тестировать не надо.
     * Так как он просто делегирует вызов другому классу.
     */
    @Test
    public void testGetAllProducts() {

    }

    /**
     * Этот метод тестировать не надо.
     * Так как он просто делегирует вызов другому классу.
     */
    @Test
    public void testGetProductByName() {

    }

    /**
     * Тестирование метода получения корзины.
     * Проверяем, что для одного покупателя возвращается одна и та же корзина.
     * Тест упадет, тк в реализации каждый раз создается новая корзина.
     */
    @Test
    public void testGetCart() {
        Cart cart1 = shoppingService.getCart(customer);
        Cart cart2 = shoppingService.getCart(customer);

        Assertions.assertEquals(cart1, cart2);
    }

    /**
     * Тестирование успешной покупки.
     * Проверяем, что при покупке уменьшается количество товара на складе,
     * и что метод сохранения товара вызывается нужное количество раз.
     */
    @Test
    public void testBuySuccess() throws BuyException {
        Product product1 = new Product("Товар1", 10);
        Product product2 = new Product("Товар2", 9);
        cart.add(product1, 3);
        cart.add(product2, 4);

        boolean result = shoppingService.buy(cart);

        Assertions.assertTrue(result);
        Assertions.assertEquals(7, product1.getCount());
        Assertions.assertEquals(5, product2.getCount());
        Mockito.verify(productDaoMock, Mockito.times(1)).save(product1);
        Mockito.verify(productDaoMock, Mockito.times(1)).save(product2);
    }

    /**
     * Тестирование, что после успешной покупки корзина очищается.
     * Тест упадет, тк в методе buy корзина не очищается.
     */
    @Test
    public void testBuyClearsCart() throws BuyException {
        Product product = new Product("Товар1", 10);
        cart.add(product, 3);

        shoppingService.buy(cart);

        Assertions.assertTrue(cart.getProducts().isEmpty());
    }

    /**
     * Тестирование покупки с пустой корзиной.
     * Проверяем, что метод возвращает false.
     */
    @Test
    public void testBuyEmptyCart() throws BuyException {
        boolean result = shoppingService.buy(cart);

        Assertions.assertFalse(result);
    }

    /**
     * Тестирование покупки, при условии, что количество товара стало нехватать,
     * после добавления в корзину.
     * Проверяем, что выбрасывается исключение BuyException,
     * и что метод сохранения товара не вызывается.
     */
    @Test
    public void testBuyInsufficientStockAfterAddToCart() {
        Product product = new Product("Товар1", 5);
        cart.add(product, 3);

        product.subtractCount(3);

        BuyException exception = Assertions.assertThrows(BuyException.class, () -> shoppingService.buy(cart));

        Assertions.assertEquals("В наличии нет необходимого количества товара 'Товар1'",
                exception.getMessage());
        Mockito.verify(productDaoMock, Mockito.never()).save(Mockito.any(Product.class));
    }

    /**
     * Тестирование покупки, когда количество товара в корзине
     * равно количеству товара на складе.
     * Проверяем, что покупка успешна, и количество товара становится нулём.
     * Тест упадет, тк в корзине проверка неверная.
     */
    @Test
    public void testBuyWhenProductCountEqualsCart() throws BuyException {

        Product product = new Product("Товар1", 3);
        cart.add(product, 3);

        boolean result = shoppingService.buy(cart);

        Assertions.assertTrue(result);
        Assertions.assertEquals(0, product.getCount());
        Mockito.verify(productDaoMock, Mockito.times(1)).save(product);
    }

    /**
     * Тестирование покупки с отрицательным количеством товара в корзине.
     * Проверяем, что метод возвращает false.
     * Тест упадет, тк в методе buy нет проверки на отрицательное количество.
     */
    @Test
    public void testBuyWithNegativeCountInCart() throws BuyException {
        Product product = new Product("Товар1", 5);
        cart.add(product, -2);

        Assertions.assertFalse(shoppingService.buy(cart));
    }
}

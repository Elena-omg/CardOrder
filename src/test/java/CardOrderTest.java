import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup(); // автоматически скачает драйвер под вашу ОС
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); // для CI, если хотите видеть браузер локально, удалите эту строку
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSubmitForm() {
        // Заполняем поле имени
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Сидоров");

        // Заполняем поле телефона
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");

        // Кликаем по чекбоксу согласия (находим элемент с классом checkbox__box внутри блока agreement)
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();

        // Отправляем форму
        driver.findElement(By.cssSelector("button")).click();

        // Проверяем, что появилось сообщение об успешной отправке
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }
}
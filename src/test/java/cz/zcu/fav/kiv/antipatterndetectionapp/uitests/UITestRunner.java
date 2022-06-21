package cz.zcu.fav.kiv.antipatterndetectionapp.uitests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Main class for UI testing scenarios
 *
 * @author Petr Urban
 * @version 1.0
 */
public class UITestRunner {

    private static final int PORT = 8080;
    private static final String WEBSITE_APP_URL = "http://localhost";
    private static String url;

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        url = WEBSITE_APP_URL + ":" + PORT;
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void testOpenSpadeApplication() {
        // init testing if the driver can open up the browser and the running application in it.
        driver.get(url);
        String title = driver.getTitle();
        assertThat(title.toLowerCase().contains("spade"));
    }

}

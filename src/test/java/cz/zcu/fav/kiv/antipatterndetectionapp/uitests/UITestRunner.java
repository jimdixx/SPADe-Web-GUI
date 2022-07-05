package cz.zcu.fav.kiv.antipatterndetectionapp.uitests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    static WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        url = WEBSITE_APP_URL + ":" + PORT;
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    @Test
    void testOpenSpadeApplication() {
        // init testing if the driver can open up the browser and the running application in it.
        driver.get(url);
        String title = driver.getTitle();
        assertThat(title.equalsIgnoreCase("spade"));
    }

    @Test
    void testDetectPage() {
        WebElement headerProjects = driver.findElement(By.xpath("//h1[contains(text(), 'Projects')]"));
        WebElement headerAntipatterns = driver.findElement(By.xpath("//h1[contains(text(), 'Anti-patterns')]"));

        Assertions.assertNotNull(headerProjects);
        Assertions.assertNotNull(headerAntipatterns);

        // GET LIST OF ALL ELEMENTS FOUND USING XPATH!
        List<WebElement> projects = driver.findElements(By.xpath("//div[@class='row']//tr//td//a[contains(@href, '/projects/')]"));
        List<WebElement> antipatterns = driver.findElements(By.xpath("//div[@class='row']//tr//td//a[contains(@href, '/anti-patterns/')]"));

        // currently exists 15 projects on the detection page
        Assertions.assertEquals(15, projects.size());

        // currently exists 10 antipatterns on the detection page
        Assertions.assertEquals(10, antipatterns.size());

        // Detect button is enabled (exists on the page and is clickable)
        WebElement detectButton = driver.findElement(By.xpath("//button[@id='analyzeButton']"));
        Assertions.assertTrue(detectButton.isEnabled());

        // check if Quick Select (Select All) works for projects
        WebElement quickSelectProjects = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@id, 'select_all_projects')]"));
        if (!quickSelectProjects.isSelected()) {
            quickSelectProjects.click();
        }

        projects = driver.findElements(By.xpath("//div[@class='row']//tr//td//a[contains(@href, '/projects/')]"));
        long selectedCheckboxes = projects.stream().peek(WebElement::isSelected).count();
        Assertions.assertEquals(projects.size(), selectedCheckboxes);

        // check if Quick Select (Select All) works for anti patterns
        WebElement quickSelectAntipatterns = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@id, 'select_all_anti_patterns')]"));
        if (!quickSelectProjects.isSelected()) {
            quickSelectProjects.click();
        }

        antipatterns = driver.findElements(By.xpath("//div[@class='row']//tr//td//a[contains(@href, '/anti-patterns/')]"));
        selectedCheckboxes = antipatterns.stream().peek(WebElement::isSelected).count();
        Assertions.assertEquals(antipatterns.size(), selectedCheckboxes);

    }

}

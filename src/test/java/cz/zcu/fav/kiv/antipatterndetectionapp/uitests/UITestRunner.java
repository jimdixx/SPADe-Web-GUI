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

    @Test
    void testConfigurationPage() {
        driver.get(url + "/configuration");

        List<WebElement> elements = driver.findElements(By.xpath("//a[@class='ap-configuration-header']"));
        // Check if there are 10 options to configure! CAN BE CHANGED IN THE TIME, SO
        // THIS PART OF THE CODE WILL NEED TO BE EXTENDED.
        Assertions.assertEquals(10, elements.size());


        //// PREPARE VARIABLES \\\\


        WebElement clickableHeader;
        WebElement numbuerValue;
        double parsedNumValue;
        WebElement stringValue;
        String substringValue;
        WebElement numberValue2;
        double parsedNumValue2;


        //// ------------------------- \\\\

        // BUSINESS AS USUAL --> Testing Default.json values!!!!
        driver.findElement(By.xpath("//a[@href='#BusinessAsUsual']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='divisionOfIterationsWithRetrospective']"));
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsWithRetrospective']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        substringValue = stringValue.getAttribute("value");

        Assertions.assertEquals(66.66, parsedNumValue);
        Assertions.assertEquals("%retr%||%revi%||%week%scrum%", substringValue);

        // BYSTANDER APATHY
        driver.findElement(By.xpath("//a[@href='#BystanderApathy']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='maximumPercentageOfTasksWithoutTeamwork']"));
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsInvalidContributors']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        substringValue = stringValue.getAttribute("value");

        Assertions.assertEquals(30, parsedNumValue);
        Assertions.assertEquals("%dependabot%", substringValue);

        // LONG OR NON EXISTENT FEEDBACK LOOPS
        driver.findElement(By.xpath("//a[@href='#LongOrNonExistentFeedbackLoops']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='divisionOfIterationsWithFeedbackLoop']"));
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsWithFeedbackLoop']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='maxGapBetweenFeedbackLoopRate']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        substringValue = stringValue.getAttribute("value");
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(50.00, parsedNumValue);
        Assertions.assertEquals("%schůz%zákazník%||%předvedení%zákazník%||%zákazn%demo%||%schůz%zadavat%||%inorm%schůz%||%zákazn%||%zadavatel%", substringValue);
        Assertions.assertEquals(2, parsedNumValue2);

        // NINETY NINETY RULE
        driver.findElement(By.xpath("//a[@href='#NinetyNinetyRule']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='maxDivisionRange']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='maxBadDivisionLimit']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(1.25, parsedNumValue);
        Assertions.assertEquals(2, parsedNumValue2);


        // ROAD TO NOWHERE
        driver.findElement(By.xpath("//a[@href='#RoadToNowhere']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='minNumberOfWikiPagesWithProjectPlan']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='minNumberOfActivitiesWithProjectPlan']"));
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsWithProjectPlan']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        substringValue = stringValue.getAttribute("value");
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(1, parsedNumValue);
        Assertions.assertEquals(1, parsedNumValue2);
        Assertions.assertEquals("%plán projektu%||%project plan%||%plan project%||%projektový plán%", substringValue);

        // SPECIFY NOTHING
        driver.findElement(By.xpath("//a[@href='#SpecifyNothing']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='minNumberOfWikiPagesWithSpecification']"));
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsWithProjectSpecification']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='minAvgLengthOfActivityDescription']"));
        WebElement numberValue3 = driver.findElement(By.xpath("//input[@id='minNumberOfActivitiesWithSpecification']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        substringValue = stringValue.getAttribute("value");
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));
        double parsedNumValue3 = Double.parseDouble(numberValue3.getAttribute("value"));

        Assertions.assertEquals(1, parsedNumValue);
        Assertions.assertEquals("%dsp%||%speciikace%||%speciication%||%vize%proj%||%vize%produ%", substringValue);
        Assertions.assertEquals(150, parsedNumValue2);
        Assertions.assertEquals(1, parsedNumValue3);

        // TOO LONG SPRINT
        driver.findElement(By.xpath("//a[@href='#TooLongSprint']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='maxIterationLength']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='maxNumberOfTooLongIterations']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(21, parsedNumValue);
        Assertions.assertEquals(0, parsedNumValue2);

        // UKNOWN POSTER
        driver.findElement(By.xpath("//a[@href='#UnknownPoster']")).click(); // click on the header so the section opens up.
        stringValue = driver.findElement(By.xpath("//input[@id='searchSubstringsInvalidNames']"));
        substringValue = stringValue.getAttribute("value");
        Assertions.assertEquals("%unknown%||%anonym%", substringValue);

        // VARYING SPRINT LENGTH
        driver.findElement(By.xpath("//a[@href='#VaryingSprintLength']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='maxDaysDifference']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='maxIterationChanged']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(7, parsedNumValue);
        Assertions.assertEquals(1, parsedNumValue2);

        // YET ANOTHER PROGRAMMER
        driver.findElement(By.xpath("//a[@href='#YetAnotherProgrammer']")).click(); // click on the header so the section opens up.
        numbuerValue = driver.findElement(By.xpath("//input[@id='numberOfFirstMonthsWithoutDetection']"));
        numberValue2 = driver.findElement(By.xpath("//input[@id='maxNumberOfNewContributors']"));

        parsedNumValue = Double.parseDouble(numbuerValue.getAttribute("value"));
        parsedNumValue2 = Double.parseDouble(numberValue2.getAttribute("value"));

        Assertions.assertEquals(2, parsedNumValue);
        Assertions.assertEquals(5, parsedNumValue2);
    }
}

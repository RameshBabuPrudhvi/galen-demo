package org.example.tests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AmazonHomePageTest {

    private WebDriver driver;
    private static final String URL = "https://www.amazon.in/";
    private static final String SPEC_PATH = "src/test/resources/specs/amazon-homepage.gspec";
    private static final String REPORT_PATH = "target/galen-reports";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1600, 1200));
    }

    @Test
    public void testAmazonHomepageLayout() throws Exception {
        // Navigate to the URL
        driver.get(URL);

        // Run the layout check and create a report
        LayoutReport layoutReport = Galen.checkLayout(driver, SPEC_PATH, List.of("desktop"));

        // Create a list of tests
        GalenTestInfo test = GalenTestInfo.fromString("Amazon India Homepage layout test");
        test.getReport().layout(layoutReport, "Check layout of Amazon India Homepage");

        // Build an HTML report
        new HtmlReportBuilder().build(List.of(test), REPORT_PATH);
    }

    @AfterClass
    public void tearDown() {
        // Quit the driver
        if (driver != null) {
            driver.quit();
        }
    }
}


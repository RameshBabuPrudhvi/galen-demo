package org.example.tests;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestFixer {
    WebDriver driver;
    static final String REPORT_PATH = "target/galen-reports";
    boolean isCurrentEnvironment = true;
    static List<GalenTestInfo> testInfo = new CopyOnWriteArrayList<>();

    @Parameters("browser")
    @BeforeTest
    public void setUp(String browser) {
        if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
            driver = new FirefoxDriver();
        } else {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(1200, 800));
    }

    @AfterTest
    public void tearDown() {
        // Quit the driver
        if (driver != null) {
            driver.quit();
        }

    }

    @AfterSuite
    public void afterSuite() throws IOException {
        new HtmlReportBuilder().build(testInfo, REPORT_PATH);
    }

}

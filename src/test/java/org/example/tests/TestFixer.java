package org.example.tests;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestFixer {
    WebDriver driver;
    static final String REPORT_PATH = "target/galen-reports";
    List<GalenTestInfo> testInfo = new ArrayList<>();
    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(1200, 800));
    }


    @AfterClass
    public void tearDown() throws IOException {
        // Quit the driver
        if (driver != null) {
            driver.quit();
        }
        new HtmlReportBuilder().build(testInfo, REPORT_PATH);
    }
}

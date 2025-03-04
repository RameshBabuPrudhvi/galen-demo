package org.example.tests;

import org.example.DriverManager;
import org.example.GalenReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.IOException;

public class TestFixer {
    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        DriverManager.initializeDriver(browser);
    }

    @AfterTest
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void afterSuite() throws IOException {
        GalenReporter.generateReport();
    }

}

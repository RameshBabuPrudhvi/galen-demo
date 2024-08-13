package org.example.tests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import org.testng.annotations.Test;

import java.util.List;

public class AmazonHomePageTest extends TestFixer {


    private static final String URL = "https://www.amazon.in/";
    private static final String SPEC_PATH = "src/test/resources/specs/amazon-homepage.gspec";

    @Test
    public void testAmazonHomePageLayout() throws Exception {
        // Navigate to the URL
        driver.get(URL);

        // Run the layout check and create a report
        var layoutReport = Galen.checkLayout(driver, SPEC_PATH, List.of("desktop"));

        // Create a list of tests
        var test = GalenTestInfo.fromString("Amazon India Homepage layout test");
        test.getReport().layout(layoutReport, "Check layout of Amazon India Homepage");
        testInfo.add(test);
    }

}


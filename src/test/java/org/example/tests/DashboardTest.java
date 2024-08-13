package org.example.tests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class DashboardTest extends TestFixer {

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {
        return new Object[][]{
                {"http://localhost:4200/#/dashboard", "src/test/resources/specs/dashboard-page-expected.gspec", "Dashboard layout test"},
                {"http://localhost:4200/#/table-list-a", "src/test/resources/specs/table-page-expected.gspec", "Table layout test"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(String url, String specPath, String testName) throws Exception {
        // Navigate to the URL
        driver.get(url);

        // Run the layout check and create a report
        var layoutReport = Galen.checkLayout(driver, specPath, List.of("desktop"));

        var test = GalenTestInfo.fromString(testName);
        test.getReport().layout(layoutReport, testName);
        testInfo.add(test);
    }
}

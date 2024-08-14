package org.example.tests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class DashboardTest extends TestFixer {
    private static final String BASE_URL = "http://localhost:4200/#/";

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {
        String dashboardUrl = BASE_URL + (isCurrentEnvironment ? "dashboard" : "dashboard-b");
        String tableUrl = BASE_URL + (isCurrentEnvironment ? "table-list-a" : "table-list-b");

        return new Object[][]{
                //{DASHBOARD_URL, "src/test/resources/specs/dashboard-page-expected.gspec", "Dashboard layout test"},
                {dashboardUrl, "src/test/resources/specs/size.gspec", "Dashboard Dimensions Validation Test", "sizeCheck"},
                {dashboardUrl, "src/test/resources/specs/position.gspec", "Dashboard Position Validation Test", "positionCheck"},
                {dashboardUrl, "src/test/resources/specs/aligned.gspec", "Dashboard Alignment Validation Test", "alignmentCheck"},
                {dashboardUrl, "src/test/resources/specs/font.gspec", "Dashboard Font Validation Test", "fontCheck"},
                {dashboardUrl, "src/test/resources/specs/logo.gspec", "Image Comparison Test", "imgCheck"},
                {tableUrl, "src/test/resources/specs/color.gspec", "Table Color Validation Test", "colorCheck"},
                {tableUrl, "src/test/resources/specs/tableLayout.gspec", "Table Layout Validation Test", "layoutCheck"}
                //{TABLE_URL, "src/test/resources/specs/table-page-expected.gspec", "Table layout test"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(String url, String specPath, String testName, String groupName) throws Exception {
        // Navigate to the URL
        driver.get(url);
        // Run the layout check and create a report
        var layoutReport = Galen.checkLayout(driver, specPath, List.of("desktop"));

        var test = GalenTestInfo.fromString(testName, List.of(groupName));

        test.getReport().layout(layoutReport, testName);

        testInfo.add(test);

        if (layoutReport.errors() > 0) {
            var errorMessage = new StringBuilder("Layout errors found: ");
            errorMessage.append(layoutReport.errors()).append("\n");
            layoutReport.getValidationErrorResults().forEach(error ->
                    errorMessage.append(error.getError().getMessages()).append("\n")
            );
            assertEquals(layoutReport.errors(), 0, errorMessage.toString());
        }
    }
}

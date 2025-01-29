package org.example.tests;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DashboardTest extends VisualTest {

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {
        String dashboardUrl = BASE_URL + (isCurrentEnvironment ? "dashboard" : "dashboard-b");
        String tableUrl = BASE_URL + (isCurrentEnvironment ? "table-list-a" : "table-list-b");

        return new Object[][]{
                {dashboardUrl, "src/test/resources/specs/size.gspec", "Dashboard Dimensions Validation Test", "sizeCheck"},
                {dashboardUrl, "src/test/resources/specs/position.gspec", "Dashboard Position Validation Test", "positionCheck"},
                {dashboardUrl, "src/test/resources/specs/aligned.gspec", "Dashboard Alignment Validation Test", "alignmentCheck"},
                {dashboardUrl, "src/test/resources/specs/font.gspec", "Dashboard Font Validation Test", "fontCheck"},
                {dashboardUrl, "src/test/resources/specs/logo.gspec", "Image Comparison Test", "imgCheck"},
                {tableUrl, "src/test/resources/specs/color.gspec", "Table Color Validation Test", "colorCheck"},
                {tableUrl, "src/test/resources/specs/tableLayout.gspec", "Table Layout Validation Test", "layoutCheck"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(ITestContext context, String url, String specPath, String testName, String groupName) throws Exception {
        verifyPageLayout(context, url, specPath, testName, groupName);
    }
}

package org.example.tests;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DashboardTest extends VisualTest {

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {
        String dashboardUrl = BASE_URL + (isCurrentEnvironment ? "dashboard" : "dashboard-b");
        String tableUrl = BASE_URL + (isCurrentEnvironment ? "table-list-a" : "table-list-b");
        String specPath = "src/test/resources/specs/demo/";
        return new Object[][]{
                {dashboardUrl, specPath + "size.gspec", "Dashboard Dimensions Validation Test", "sizeCheck"},
                {dashboardUrl, specPath + "position.gspec", "Dashboard Position Validation Test", "positionCheck"},
                {dashboardUrl, specPath + "aligned.gspec", "Dashboard Alignment Validation Test", "alignmentCheck"},
                {dashboardUrl, specPath + "font.gspec", "Dashboard Font Validation Test", "fontCheck"},
                {dashboardUrl, specPath + "logo.gspec", "Image Comparison Test", "imgCheck"},
                {tableUrl, specPath + "color.gspec", "Table Color Validation Test", "colorCheck"},
                {tableUrl, specPath + "tableLayout.gspec", "Table Layout Validation Test", "layoutCheck"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(ITestContext context, String url, String specPath, String testName, String groupName) throws Exception {
        verifyPageLayout(context, url, specPath, testName, groupName);
    }
}

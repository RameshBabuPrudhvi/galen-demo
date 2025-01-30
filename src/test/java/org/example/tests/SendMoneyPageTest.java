package org.example.tests;

import org.example.DriverManager;
import org.example.GalenHelper;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SendMoneyPageTest extends TestFixer {

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {
        String sendMoneyUrl = "https://www.westernunion.com/in/en/web/send-money/start";
        String specPath = "src/test/resources/specs/wu/";
        return new Object[][]{
                {sendMoneyUrl, specPath + "demo.gspec", "Send Money Page Dimensions Validation Test", "demoCheck"},
                {sendMoneyUrl, specPath + "logo.gspec", "Logo Test", "imgCheck"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(ITestContext context, String url, String specPath, String testName, String groupName) {
        DriverManager.getDriver().get(url);
        GalenHelper.verifyPageLayout(context, specPath, testName, groupName);
    }
}

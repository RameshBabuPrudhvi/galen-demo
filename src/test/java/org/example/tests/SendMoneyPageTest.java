package org.example.tests;

import org.example.DriverManager;
import org.example.GalenHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SendMoneyPageTest extends TestFixer {
    public static final String URL = "https://www.westernunion.com/in/en/web/send-money/start";

    @DataProvider(name = "layoutTestData")
    public Object[][] layoutTestData() {

        return new Object[][]{
                {"sendMoney.gspec", "Send Money Page Dimensions Validation Test", "demoCheck"},
                {"logo.gspec", "Logo Test", "imgCheck"}
        };
    }

    @Test(dataProvider = "layoutTestData")
    public void testPageLayout(ITestContext context, String specFile, String testName, String groupName) {
        String specPath = "src/test/resources/specs/wu/";
        GalenHelper.verifyPageLayout(context, specPath + specFile, testName, groupName);
    }

    @BeforeTest
    public void login() {
        var driver = DriverManager.getDriver();
        driver.get(URL);
        var wait = new WebDriverWait(driver, 30);
        wait.until(d -> d.findElement(By.id("btn_lead_registration_cancel")).isDisplayed());
        driver.findElement(By.id("btn_lead_registration_cancel")).click();
        driver.findElement(By.xpath("//li[normalize-space()='United States']")).click();
        driver.findElement(By.id("btn_country_selection_continue")).click();
        wait.until(driver1 -> driver1.findElement(By.id("label_estimate_details_selected_country")).isDisplayed());
        driver.findElement(By.id("input-estimate_details_sender_field")).sendKeys("1000");
        wait.until(driver1 -> driver1.findElement(By.id("select-dropdown_estimate_details_payin")).isDisplayed());
        driver.findElement(By.id("overlay_for_fifo_redesign_first_time_exp")).click();
    }
}

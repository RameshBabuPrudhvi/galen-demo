package org.example.tests;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import org.example.TestContext;
import org.testng.ITestContext;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class VisualTest extends TestFixer {

    public void verifyPageLayout(ITestContext context, String url, String specPath, String testName, String groupName) throws Exception {
        var browserName = TestContext.getBrowserName();
        var newTestName = testName + "-" + browserName;

        driver.get(url);
        // Run the layout check and create a report
        var layoutReport = Galen.checkLayout(driver, specPath, List.of("desktop"));
        context.setAttribute("layoutReport-" + browserName, layoutReport);
        var test = GalenTestInfo.fromString(newTestName, List.of(groupName));

        test.getReport().layout(layoutReport, newTestName);

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

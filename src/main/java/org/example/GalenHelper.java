package org.example;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import lombok.SneakyThrows;
import org.testng.ITestContext;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class GalenHelper {

    @SneakyThrows
    public static void verifyPageLayout(ITestContext context, String specPath, String testName, String groupName) {
        var driver = DriverManager.getDriver();
        var browserName = driver.getClass().getSimpleName().toLowerCase().replace("driver", "");
        String newTestName = testName + "-" + browserName;

        var layoutReport = Galen.checkLayout(driver, specPath, List.of("desktop", browserName));
        context.setAttribute("layoutReport-" + browserName, layoutReport);
        var test = GalenTestInfo.fromString(newTestName, List.of(groupName));

        test.getReport().layout(layoutReport, newTestName);
        GalenReporter.addTestInfo(test);

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

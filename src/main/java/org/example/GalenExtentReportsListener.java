package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.reports.model.LayoutSection;
import com.galenframework.reports.model.LayoutSpec;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GalenExtentReportsListener implements ITestListener {

    private static ExtentReports extent;
    private final ThreadLocal<ExtentTest> test = new InheritableThreadLocal<>();
    private static final String GALEN_REPORT_PATH = "target/galen-reports/";

    @Override
    public void onStart(ITestContext context) {

        var sparkReporter = new ExtentSparkReporter(GALEN_REPORT_PATH + "email-report.html");
        sparkReporter.config().setJs(
                "document.querySelectorAll('.col-md-3 h6.card-title').forEach((element) => { " +
                        "    if (element.textContent.trim() === 'Steps') { " +
                        "        element.closest('.col-md-3').style.setProperty('display', 'none'); " +
                        "    } " +
                        "}); " +
                        "document.querySelectorAll('.col-md-3 h6.card-title')[2].innerText = 'Steps';"
        );
        sparkReporter.config().setReportName("Galen Report");
        sparkReporter.viewConfigurer().viewOrder().as(List.of(ViewName.DASHBOARD, ViewName.TEST,
                ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.AUTHOR, ViewName.DEVICE, ViewName.LOG));
        extent = new ExtentReports();

        extent.attachReporter(sparkReporter);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();

    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getParameters()[3].toString() + "-" + TestContext.getBrowserName() + UUID.randomUUID());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logGalenLayoutReport(test.get(), result);
        test.get().pass("Test passed");
        test.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logGalenLayoutReport(test.get(), result);
        test.get().fail(result.getName());
        test.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
        test.remove();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented
        test.remove();
    }

    private synchronized void logGalenLayoutReport(ExtentTest extentTest, ITestResult result) {
        var browserName = TestContext.getBrowserName();
        LayoutReport layoutReport = (LayoutReport) result.getTestContext().getAttribute("layoutReport-" + browserName);
        if (layoutReport != null) {

            for (LayoutSection section : layoutReport.getSections()) {
                extentTest.info("Layout Report for " + browserName + " - " + result.getMethod().getMethodName());

                ExtentTest sectionTest = extentTest.createNode("Section: " + section.getName());

                // Create a map to group results by object
                Map<String, List<LayoutSpec>> objectSpecsMap = new HashMap<>();

                // Populate the map with object names and their specs
                section.getObjects().forEach(layoutObject -> {
                    var objectName = layoutObject.getName();
                    List<LayoutSpec> specs = new ArrayList<>(layoutObject.getSpecs());
                    objectSpecsMap.put(objectName, specs);
                });

                // Iterate through the map and log results grouped by object
                objectSpecsMap.forEach((objectName, specs) -> {
                    ExtentTest objectTest = sectionTest.createNode(objectName);

                    // Group specs by their status
                    Map<String, List<LayoutSpec>> specsByStatus = specs.stream()
                            .collect(Collectors.groupingBy(spec -> spec.getStatus().toString()));

                    specsByStatus.forEach((status, statusSpecs) -> {

                        statusSpecs.forEach(spec -> {

                            if (status.equalsIgnoreCase("error")) {
                                var errorMessage = spec.getErrors().toString();
                                objectTest.fail(spec.getName());
                                objectTest.log(Status.FAIL, errorMessage);

                                if (spec.getImageComparison() != null) {
                                    var actualImg = Path.of(GALEN_REPORT_PATH + spec.getImageComparison().getActualImage());
                                    var expectedImg = Path.of(GALEN_REPORT_PATH + spec.getImageComparison().getExpectedImage());
                                    var comparisonMapImg = Path.of(GALEN_REPORT_PATH + spec.getImageComparison().getComparisonMapImage());

                                    objectTest.addScreenCaptureFromBase64String(encodeImage(actualImg), "Actual Image");
                                    objectTest.addScreenCaptureFromBase64String(encodeImage(expectedImg), "Expected Image");
                                    objectTest.addScreenCaptureFromBase64String(encodeImage(comparisonMapImg), "Comparison Map Image");
                                }
                            } else {
                                objectTest.pass(spec.getName());
                            }
                            objectTest.assignCategory(getCategory(spec.getName()));
                        });
                    });
                });
            }
        }
    }

    private static String encodeImage(Path imagePath) {
        try {
            var imageBytes = Files.readAllBytes(imagePath);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCategory(String specName) {
        Map<String, Set<String>> categories = new HashMap<>();
        categories.put("position", Set.of("margin", "padding", "above", "below", "right-of", "left-of"));
        categories.put("color", Set.of("color", "background"));
        categories.put("visibility", Set.of("visibility"));
        categories.put("font", Set.of("font"));
        categories.put("text", Set.of("text"));
        categories.put("border", Set.of("border", "border-width", "border-color", "border-style", "border-radius"));
        categories.put("size", Set.of("size", "width", "height"));
        categories.put("image", Set.of("image"));
        categories.put("spacing", Set.of("margin", "padding", "spacing"));
        categories.put("layout", Set.of("display", "position", "float", "clear"));
        categories.put("shadow", Set.of("box-shadow", "text-shadow"));
        categories.put("transform", Set.of("transform", "rotate", "scale", "translate"));
        categories.put("opacity", Set.of("opacity"));
        categories.put("overflow", Set.of("overflow", "overflow-x", "overflow-y"));
        categories.put("alignment", Set.of("align", "top", "left", "right", "bottom", "horizontally", "vertically"));
        categories.put("flex", Set.of("flex", "flex-direction", "flex-wrap", "justify-content", "align-items", "align-content"));
        categories.put("grid", Set.of("grid", "grid-template-columns", "grid-template-rows", "grid-column", "grid-row"));

        return categories.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .anyMatch(specName::contains))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("unknown");
    }
}

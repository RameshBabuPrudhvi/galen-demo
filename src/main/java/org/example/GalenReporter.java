package org.example;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GalenReporter {
    private static final String REPORT_PATH = "target/galen-reports";
    private static final List<GalenTestInfo> testInfo = new CopyOnWriteArrayList<>();

    public static void addTestInfo(GalenTestInfo test) {
        testInfo.add(test);
    }

    public static void generateReport() throws IOException {
        new HtmlReportBuilder().build(testInfo, REPORT_PATH);
    }
}

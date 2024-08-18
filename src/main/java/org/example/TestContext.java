package org.example;

public class TestContext {
    private static final ThreadLocal<String> browserName = new ThreadLocal<>();

    public static void setBrowserName(String browser) {
        browserName.set(browser);
    }

    public static String getBrowserName() {
        return browserName.get();
    }

    public static void clear() {
        browserName.remove();
    }
}


package org.example.tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ImgComparisonTest {
    private WebDriver driver;
    private static final String URL = "https://www.amazon.in/";
    private static final String BASE_IMAGE_PATH = "src/test/resources/baseImage.png";
    private static final String NEW_IMAGE_PATH = "src/test/resources/newImage.png";
    private static final String DIFF_IMAGE_PATH = "src/test/resources/diffImage.png";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test

    public void imgDiff() throws IOException {
        driver.get(URL);
        var screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        var newImageFile = Path.of(NEW_IMAGE_PATH);
        Files.copy(screenshot.toPath(), newImageFile, REPLACE_EXISTING);
        // Load images
        var baseImage = ImageIO.read(new File(BASE_IMAGE_PATH));
        var newImage = ImageIO.read(newImageFile.toFile());

        // Compare images
        var diffImage = ImageComparator.diff(baseImage, newImage);

        if (diffImage.isPresent()) {
            // Save the diff image
            ImageIO.write(diffImage.get(), "png", new File(DIFF_IMAGE_PATH));
            System.out.println("Images are different. Differences highlighted in diffImage.png.");
        } else {
            System.out.println("Images are identical.");
        }

        driver.quit();
    }
}

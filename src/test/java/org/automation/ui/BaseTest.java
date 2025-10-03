package org.automation.ui;

import org.apache.commons.io.FileUtils;
import org.automation.utils.ScreenshotUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // ✅ Detect environment – if running in CI (GitHub Actions), use headless
        String ciEnv = System.getenv("CI");
        boolean isCI = ciEnv != null && ciEnv.equalsIgnoreCase("true");

        if (isCI) {
            System.out.println("🚀 Running in CI mode – enabling headless Chrome...");
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        } else {
            System.out.println("🖥️ Running locally – launching full Chrome browser...");
            options.addArguments("--start-maximized");
        }

        // Optional: use CHROME_BIN and CHROMEDRIVER_BIN if provided by CI
        String chromeBinary = System.getenv("CHROME_BIN");
        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        }

        WebDriver webDriver = new ChromeDriver(options);
        DriverManager.setDriver(webDriver); // Save driver in ThreadLocal
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();

        // ✅ Capture screenshot ONLY if test failed
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            String testName = result.getMethod().getMethodName();
            ScreenshotUtils.capture(driver, testName);
        }

        if (driver != null) {
            driver.quit();
            DriverManager.removeDriver();
        }
    }

    // Manual screenshot helper
    public void takeScreenshot(String name) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return;

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("target/screenshots/" + name + ".png");
        destFile.getParentFile().mkdirs();
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

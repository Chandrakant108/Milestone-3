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
        // ✅ Use environment variables for CI paths
        String chromeBinary = System.getenv("CHROME_BIN");
        String chromeDriver = System.getenv("CHROMEDRIVER_BIN");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");

        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        }

        if (chromeDriver != null && !chromeDriver.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriver);
        }

        WebDriver driver = new ChromeDriver(options);
        DriverManager.setDriver(driver); // Save driver in ThreadLocal
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Tear down driver and capture screenshot ONLY on failure.
     */
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

    // Optional: Manual screenshot helper
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

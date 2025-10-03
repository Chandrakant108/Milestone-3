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

        // ✅ Auto-detect Chrome binary from environment or fallback
        String chromePath = System.getenv("CHROME_BIN");
        if (chromePath != null && !chromePath.isEmpty()) {
            options.setBinary(chromePath);
        }

        // ✅ Optional: useful flags for CI to avoid crashes
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // ❌ Headless mode is NOT added — this runs Chrome with UI

        // ✅ Auto-detect ChromeDriver path from environment
        String driverPath = System.getenv("CHROMEDRIVER_BIN");
        if (driverPath != null && !driverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", driverPath);
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

        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            String testName = result.getMethod().getMethodName();
            ScreenshotUtils.capture(driver, testName);
        }

        if (driver != null) {
            driver.quit();
            DriverManager.removeDriver();
        }
    }

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

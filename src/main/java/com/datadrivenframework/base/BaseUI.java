package com.datadrivenframework.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.datadrivenframework.utils.DateUtils;
import com.datadrivenframework.utils.ExtentReportManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseUI {

    public WebDriver driver;
    public Properties prop;
    public ExtentReports report = ExtentReportManager.getReportInstance();
    public ExtentTest logger;

    public void invokeBrowser(String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("opera")) {
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/Applications/Opera.app");
            driver = new ChromeDriver(options);
        } else {
            driver = new SafariDriver();
        }

        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();
        }

        if (prop == null) {
            prop = new Properties();
            try {
                FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/ObjectRepository/projectConfig.properties");
                prop.load(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void openURL(String websiteURL) {
        driver.get(prop.getProperty(websiteURL));
    }

    public void enterText(String xpath, String data) {
        getElement(xpath).sendKeys(data);
    }

    public void click(String xpath) {
        getElement(xpath).click();
    }

    public void quitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void tearDown() {
        quitBrowser();
    }

    public WebElement getElement(String locator) {
        WebElement element = null;
        try {
            if (locator.endsWith("_id")) {
                element = driver.findElement(By.id(prop.getProperty(locator)));
            } else if (locator.endsWith("_xpath")) {
                element = driver.findElement(By.xpath(prop.getProperty(locator)));
            } else if (locator.endsWith("_CSS")) {
                element = driver.findElement(By.cssSelector(prop.getProperty(locator)));
            } else if (locator.endsWith("_linkText")) {
                element = driver.findElement(By.linkText(prop.getProperty(locator)));
            } else if (locator.endsWith("_partialLinkText")) {
                element = driver.findElement(By.partialLinkText(prop.getProperty(locator)));
            } else if (locator.endsWith("_name")) {
                element = driver.findElement(By.name(prop.getProperty(locator)));
            } else {
                reportFail("Failing the testcase. Invalid locator " + locator);
                Assert.fail("Failing the testcase. Invalid locator " + locator);
            }
        } catch (Exception e) {
            reportFail(e.getMessage());
            e.printStackTrace();
            Assert.fail("Failing the test case: " + e.getMessage());
        }
            return element;
        }

        public void reportFail (String reportString){

        }

        public void reportPass (String reportString){

        }

        public void takeScreenshotOnFailure() {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(System.getProperty("user.dir") + "/screenshots/" + DateUtils.getTimeStamp() + ".png");
            try {
                FileUtils.copyFile(sourceFile, destFile);
                logger.addScreenCaptureFromPath(System.getProperty("user.dir") + "/screenshots/" + DateUtils.getTimeStamp() + ".png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
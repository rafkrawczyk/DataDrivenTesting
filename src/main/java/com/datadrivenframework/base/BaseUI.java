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
import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;

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

    SoftAssert softAssert = new SoftAssert();

    public void invokeBrowser(String browserName) {
        try {
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
        } catch (Exception e) {
            reportFail(e.getMessage());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

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
        try {
            driver.get(prop.getProperty(websiteURL));
            reportPass(websiteURL + " identified successfully");
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void enterText(String xpath, String data) {
        try {
            getElement(xpath).sendKeys(data);
            reportPass(data + " - inserted successfully in locator element " + xpath);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void click(String xpath) {
        try {
            getElement(xpath).click();
            reportPass(xpath + " - element clicked successfully");
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
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
                logger.log(Status.INFO, "Locator identified: " + locator);
            } else if (locator.endsWith("_xpath")) {
                element = driver.findElement(By.xpath(prop.getProperty(locator)));
                logger.log(Status.INFO, "Locator identified: " + locator);
            } else if (locator.endsWith("_CSS")) {
                element = driver.findElement(By.cssSelector(prop.getProperty(locator)));
                logger.log(Status.INFO, "Locator identified: " + locator);
            } else if (locator.endsWith("_linkText")) {
                element = driver.findElement(By.linkText(prop.getProperty(locator)));
                logger.log(Status.INFO, "Locator identified: " + locator);
            } else if (locator.endsWith("_partialLinkText")) {
                element = driver.findElement(By.partialLinkText(prop.getProperty(locator)));
                logger.log(Status.INFO, "Locator identified: " + locator);
            } else if (locator.endsWith("_name")) {
                element = driver.findElement(By.name(prop.getProperty(locator)));
                logger.log(Status.INFO, "Locator identified: " + locator);
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

    public boolean isElementPresent(String locator) {
        try {
            if (getElement(locator).isDisplayed()) ;
            {
                reportPass(locator + " - element is displayed.");
            }
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
        return false;
    }

    public boolean isElementSelected(String locator) {
        try {
            if (getElement(locator).isSelected()) ;
            {
                reportPass(locator + " - element is selected.");
            }
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
        return false;
    }

    public boolean isElementEnabled(String locator) {
        try {
            if (getElement(locator).isEnabled()) ;
            {
                reportPass(locator + " - element is enabled.");
            }
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
        return false;
    }

    public void assertTrue(boolean flag) {
            softAssert.assertTrue(flag);
    }

    public void assertFalse(boolean flag) {
        softAssert.assertFalse(flag);
    }

    public void assertTrue(String actual, String expected) {
        softAssert.assertEquals(actual, expected);
    }

    public void reportFail(String reportString) {
        logger.log(Status.FAIL, reportString);
        takeScreenshotOnFailure();
        Assert.fail(reportString);
    }

    public void reportPass(String reportString) {
        logger.log(Status.PASS, reportString);
    }

    @AfterMethod
    public void afterTest() {
        softAssert.assertAll();
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
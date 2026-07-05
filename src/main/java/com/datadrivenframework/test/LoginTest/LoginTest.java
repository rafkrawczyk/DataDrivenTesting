package com.datadrivenframework.test.LoginTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.datadrivenframework.base.BaseUI;
import com.datadrivenframework.utils.ExtentReportManager;
import org.testng.annotations.AfterMethod; // Added
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider; // Added
import org.testng.annotations.Test;

public class LoginTest extends BaseUI {

    @DataProvider(name = "browserProvider")
    public Object[][] getBrowsers() {
        return new Object[][] {
                {"chrome"},
                {"opera"},
                //{"safari"}
        };
    }

    @Test(dataProvider = "browserProvider")
    public void testWikipediaLogin(String browserName) {
        logger = report.createTest("Wikipedia Login Test - " + browserName.toUpperCase());
        logger.log(Status.INFO, "Initializing browser");
        invokeBrowser(browserName);
        logger.log(Status.INFO, "Opening website");
        openURL("websiteURL");
        logger.log(Status.INFO, "Clicking on signin button");
        click("logInButton_xpath");
        logger.log(Status.INFO, "Entering username and password");
        enterText("usernameTextBox_id", "K4ng4roo");
        enterText("passwordTextBox_CSS", "password");
        logger.log(Status.PASS, "Test executed successfully");
        takeScreenshotOnFailure();
    }

    @AfterTest
    public void endReport() {
        report.flush();
    }

    @AfterMethod
    public void endTest() {
        tearDown();
    }
}
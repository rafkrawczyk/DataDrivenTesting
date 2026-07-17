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
        invokeBrowser(browserName);
        openURL("websiteURL");
        click("logInButton_xpath");
        enterText("usernameTextBox_id", "K4ng4roo");
        enterText("passwordTextBox_CSS", "password");
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
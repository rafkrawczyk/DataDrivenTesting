package com.datadrivenframework.test.LoginTest;

import com.datadrivenframework.base.BaseUI;
import org.testng.annotations.AfterMethod; // Added
import org.testng.annotations.DataProvider; // Added
import org.testng.annotations.Test;

public class LoginTest extends BaseUI {

    // 1. DataProvider feeds different browsers into the test
    @DataProvider(name = "browserProvider")
    public Object[][] getBrowsers() {
        return new Object[][] {
                {"chrome"},
                {"opera"},
                {"safari"}
        };
    }

    // 2. One single, clean test method that runs for every browser in the DataProvider
    @Test(dataProvider = "browserProvider")
    public void testWikipediaLogin(String browserName) {
        invokeBrowser(browserName);
        openURL("websiteURL");

        // Cleaned XPaths (replaced \" with ')
        click("logInButton_xpath");
        enterText("usernameTextBox_xpath", "K4ng4roo");
    }

    // 3. Guaranteed to close the browser, even if the test fails midway
    @AfterMethod
    public void endTest() {
        tearDown();
    }
}
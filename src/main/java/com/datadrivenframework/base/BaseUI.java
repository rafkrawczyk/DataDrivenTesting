package com.datadrivenframework.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Properties;

public class BaseUI {

    public WebDriver driver;
    public Properties prop;

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
        driver.findElement(By.xpath(prop.getProperty(xpath))).sendKeys(data);
    }

    public void click(String xpath) {
        driver.findElement(By.xpath(prop.getProperty(xpath))).click();
    }

    public void quitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void tearDown() {
        quitBrowser();
    }
}
package com.datadrivenframework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import static java.lang.System.*;

public class ExtentReportManager {

    public static ExtentSparkReporter extentSparkReporter;
    public static ExtentReports report;

    public static ExtentReports getReportInstance() {

        if(extentSparkReporter == null && report == null) {
            String reportName = DateUtils.getTimeStamp() + ".html";
            extentSparkReporter = new ExtentSparkReporter(getProperty("user.dir") + "/testOutput/" + reportName);
            report = new ExtentReports();
            report.attachReporter(extentSparkReporter);
            report.setSystemInfo("OS", "OSX");
            report.setSystemInfo("Environment", "UAT");
            report.setSystemInfo("Browser", "Chrome");
            extentSparkReporter.config().setDocumentTitle("UAT UI Automation Report");
            extentSparkReporter.config().setReportName("All Headlines UI Test Report");
            extentSparkReporter.config().setTimeStampFormat("dd MM yyyy HH:mm:ss");
        }
        return report;
    }
}

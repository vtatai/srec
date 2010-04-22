package com.github.srec.hadoop;

import com.github.srec.ui.TestForm;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNGRunner {
    private List<ITestResult> successes = new ArrayList<ITestResult>();
    private List<ITestResult> failures = new ArrayList<ITestResult>();
    private List<ITestResult> skipped = new ArrayList<ITestResult>();
    private List<ITestResult> failureWithSuccessPercentage = new ArrayList<ITestResult>();

    private ITestListener testListener = new TestListenerAdapter() {
        @Override
        public void onTestSuccess(ITestResult iTestResult) {
            System.out.println("Test success: " + iTestResult);
            successes.add(iTestResult);
        }

        @Override
        public void onTestFailure(ITestResult iTestResult) {
            System.out.println("Test failure: " + iTestResult);
            failures.add(iTestResult);
        }

        @Override
        public void onTestSkipped(ITestResult iTestResult) {
            System.out.println("Test skipped: " + iTestResult);
            skipped.add(iTestResult);
        }

        @Override
        public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
            System.out.println("Test failed but with some success: " + iTestResult);
            failureWithSuccessPercentage.add(iTestResult);
        }
    };

    public void run(final String script) {
        XmlSuite suite = new XmlSuite();
        suite.setName("SRecSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("SRecTest");
        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass(SRecTestNGTest.class));
        test.setXmlClasses(classes);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("script", script);
        test.setParameters(parameters);

        TestNG testng = new TestNG();
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.addListener(testListener);
        testng.run();

        try {
            // Hack to close any remaining windows
            // A real TestNG runner would spawn a separate JVM so that this is not needed
            // Also notice that this is Java 6 only
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    for (Window w : Window.getOwnerlessWindows()) {
                        w.dispose();
                    }
                }
            });
        } catch (InterruptedException e) {
            throw new TestNGRunnerException(e);
        } catch (InvocationTargetException e) {
            throw new TestNGRunnerException(e);
        }
    }

    public long getSuccessCount() {
        return successes.size();
    }

    public long getFailureCount() {
        return failures.size();
    }

    public long getSkippedCount() {
        return skipped.size();
    }

    public long getFailureWithSuccessPercentageCount() {
        return failureWithSuccessPercentage.size();
    }

    public List<ITestResult> getSuccesses() {
        return successes;
    }

    public List<ITestResult> getFailures() {
        return failures;
    }

    public List<ITestResult> getSkipped() {
        return skipped;
    }

    public List<ITestResult> getFailureWithSuccessPercentage() {
        return failureWithSuccessPercentage;
    }

    public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException {
        TestForm.main(new String[]{});
        TestNGRunner testRunner = new TestNGRunner();
        testRunner.run(readFileAsString(args[0]));
        System.out.println(testRunner);
        for (ITestResult result : testRunner.getFailures()) {
            System.out.println(result);
        }
    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }

    @Override
    public String toString() {
        return "TestNGRunner{" +
                "successCount=" + getSuccessCount() +
                ", failureCount=" + getFailureCount() +
                ", skippedCount=" + getSkippedCount() +
                ", failureWithSuccessPercentageCount=" + getFailureWithSuccessPercentageCount() +
                '}';
    }
}

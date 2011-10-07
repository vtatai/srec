/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package com.github.srec.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a set of related test cases for a specific component or 
 * system.
 * 
 * @author Victor Tatai
 */
public class TestSuite {
    private String name;
    private List<TestCase> testCases = new ArrayList<TestCase>();
    private Map<String, Object> properties;

    /**
     * Creates a test suite with the given name. <p/>
     * 
     * @param name the name of the suite.
     */
    public TestSuite(String name) {
        this.name = name;
    }

    /**
     * Returns all test cases of the suite. <p/>
     * 
     * @return test cases.
     */
    public List<TestCase> getTestCases() { 
    	return testCases;
    }
    
    /**
     * Sets test cases for the suite. <p/>
     * 
     * @param testCases test cases.
     */
    public void setTestCases(List<TestCase> testCases) {
    	this.testCases = testCases;
    }

    /**
     * Adds a test case to the suite. <p/>
     * 
     * @param testCase
     */
    public void addTestCase(TestCase testCase) { 
    	testCases.add(testCase);
    }

    /**
     * Returns the name of the suite. <p/>
     * 
     * @return the name of the suite.
     */
    public String getName() {
    	return name;
    }
    
    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

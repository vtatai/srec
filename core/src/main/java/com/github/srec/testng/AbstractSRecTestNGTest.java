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

package com.github.srec.testng;

import com.github.srec.command.parser.ParseException;
import com.github.srec.play.Player;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * An abstract classe to be used when writing TestNG tests.
 * 
 * @author Victor Tatai
 */
public abstract class AbstractSRecTestNGTest {
    protected String scriptDir;
    protected String className;
    protected String[] params;

    public AbstractSRecTestNGTest(String scriptDir) {
        this.scriptDir = scriptDir;
    }

    protected AbstractSRecTestNGTest(String scriptDir, String className, String[] params) {
        this.scriptDir = scriptDir;
        this.className = className;
        this.params = params;
    }

    /**
     * Runs an entire suite or a single test case.
     *
     * @param script The name of the script to run, located inside {@link #scriptDir}
     * @param testCase The name of the test case to run
     * @param failOnError true if an error should result in a test failure
     * @return The errors
     */
    protected Player runTest(String script, String testCase, boolean failOnError) {
        try {
            Player p = new Player()
                    .init()
                    .play(new File(scriptDir + File.separator + script), testCase, className, params);
            p.printErrors();
            if (failOnError) {
                assertEquals(p.getErrors().size(), 0);
            }
            return p;
        } catch (ParseException e) {
            if (failOnError) {
                e.printErrors();
                fail();
            } else {
                throw e;
            }
        } catch (IOException e) {
            fail();
        }
        return null;
    }

    /**
     * Runs an entire suite or a single test case, failing if there is an error.
     *
     * @param script The name of the script to run, located inside {@link #scriptDir}
     * @param testCase The name of the test case to run
     * @return The errors
     */
    protected Player runTest(String script, String testCase) {
        return runTest(script, testCase, true);
    }

    /**
     * Runs an entire suite.
     *
     * @param script The name of the script to run, located inside {@link #scriptDir}
     * @param failOnError If an error should cause the test to fail
     * @return The player errors
     */
    protected Player runTest(String script, boolean failOnError) {
        return runTest(script, null, failOnError);
    }

    /**
     * Runs an entire suite, failing if there is an error.
     *
     * @param script The name of the script to run, located inside {@link #scriptDir}
     * @return The player errors
     */
    protected Player runTest(String script) {
        return runTest(script, null, true);
    }
}

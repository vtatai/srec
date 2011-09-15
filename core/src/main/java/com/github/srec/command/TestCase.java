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

/**
 * A TestCase defines a set of inputs/outputs, preconditions, 
 * postconditions and commands that are executed for a test objective.
 * They are declared in XML files.
 * 
 * @author Victor Tatai
 */
public class TestCase {
    private ExecutionContext executionContext;
    private String name;

    public TestCase(String name, ExecutionContext executionContext) {
        this.name = name;
        this.executionContext = executionContext;
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public String getName() {
        return name;
    }
}

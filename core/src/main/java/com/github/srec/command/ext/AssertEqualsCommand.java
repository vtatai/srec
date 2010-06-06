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

package com.github.srec.command.ext;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.AssertionFailedException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.value.Value;

import java.util.Map;

/**
 * A command which asserts that two general Java objects are equal.
 *
 * @author Victor Tatai
 */
@SRecCommand
public class AssertEqualsCommand extends MethodCommand {
    public AssertEqualsCommand() {
        super("assert_equals", "text", "varName");
    }

    @Override
    protected Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        String text = asString("text", params, context);
        String varName = asString("varName", params, context);
        Object object = findObjectSymbol(varName, context);
        final String actual = object.toString();
        if (!actual.equals(text)) throw new AssertionFailedException("actual: " + actual + ", expected: " + text);
        return null;        
    }

    @Override
    public boolean isNative() {
        return true;
    }
    
    @Override
    public String toString() {
        return "assert_equals";
    }
}
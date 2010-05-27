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

package com.github.srec.command.value;

/**
 * @author Victor Tatai
 */
public class BooleanValue extends Value<Boolean> {
    public static BooleanValue TRUE = new BooleanValue(true);
    public static BooleanValue FALSE = new BooleanValue(false);
    
    public BooleanValue(Boolean value) {
        super(Type.BOOLEAN, value);
    }

    public BooleanValue(String value) {
        super(Type.BOOLEAN, Boolean.valueOf(value));
    }

    public static BooleanValue getInstance(Boolean b) {
        return b ? TRUE : FALSE;
    }
}

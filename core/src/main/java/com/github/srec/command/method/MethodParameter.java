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

package com.github.srec.command.method;

import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

/**
 * Represents a method parameter.
 * @author Victor Tatai
 */
public class MethodParameter {
    private String name;
    private boolean optional;
    /**
     * Only used if parameter can be optional.
     */
    private Value defaultValue;
    /**
     * Type is required information only when the parser cannot safely determine the type of parameters at run time,
     * such as when reading from a XML file.
     */
    private Type type;

    public MethodParameter(String name) {
        this.name = name;
    }

    public MethodParameter(String name, Type type) {
        this(name);
        this.type = type;
    }

    public MethodParameter(String name, boolean optional, Value defaultValue) {
        this.name = name;
        this.optional = optional;
        this.defaultValue = defaultValue;
    }

    public MethodParameter(String name, Type type, boolean optional, Value defaultValue) {
        this(name, optional, defaultValue);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

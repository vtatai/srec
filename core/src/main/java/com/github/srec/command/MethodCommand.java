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

import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.exception.MethodDefinitionException;
import com.github.srec.command.value.Value;

/**
 * Defines a method (not a method call).
 * 
 * @author Victor Tatai
 */
public abstract class MethodCommand extends BaseCommand implements CommandSymbol {
    protected Parameter[] parameters;

    protected MethodCommand(String name, Parameter... parameters) {
        super(name);
        boolean startOptional = false;
        for (Parameter parameter : parameters) {
            assert parameter != null;
            if (startOptional && !parameter.isOptional()) {
                throw new MethodDefinitionException("All optional parameters must be defined last in method '" + name
                        + "'");
            }
            if (!startOptional && parameter.isOptional()) {
                startOptional = true;
            }
        }
        this.parameters = parameters;
    }

    protected MethodCommand(String name, String... parameters) {
        this(name, convertParameters(parameters));
    }

    private static Parameter[] convertParameters(String[] parameters) {
        Parameter[] ps = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            ps[i] = new Parameter(parameters[i]);
        }
        return ps;
    }
        
    /**
     * Executes the method call.
     *
     * @param context The execution context
     * @param params The parameters to run the method
     * @return The return value from the method call
     */
    public Value callMethod(ExecutionContext context, Value... params) {
        validateParameters(params);
        return internalCallMethod(context, params);
    }

    /**
     * Executes the method call after parameter validation.
     *
     * @param context The execution context
     * @param params The parameters to run the method
     * @return The return value from the method call
     */
    protected abstract Value internalCallMethod(ExecutionContext context, Value... params);

    @Override
    public void run(ExecutionContext context) {
        // a def command by definition does not run
    }

    /**
     * Validates the parameters based only on count.
     *
     * @param callParameters The runtime (call) params
     */
    protected void validateParameters(Value... callParameters) {
        int paramCountCall = callParameters == null ? 0 : callParameters.length;
        int paramCountDef = this.parameters == null ? 0 : this.parameters.length;
        if (paramCountCall == paramCountDef) return;
        if (paramCountCall > paramCountDef) throw new CommandExecutionException("Different argument counts");
        if (!parameters[paramCountCall].isOptional()) throw new CommandExecutionException("Different argument counts");
    }

    /**
     * Return true if the implementation for this command is native, ie, implemented directly in Java.
     *
     * @return true if native, false otherwise
     */
    public boolean isNative() {
        return false;
    }

    /**
     * Gets a parameter by name.
     * 
     * @param name The name
     * @param callParams The call parameters
     * @return The parameter value
     */
    public Value get(String name, Value[] callParams) {
        int i;
        Parameter parameter = null;
        for (i = 0; i < parameters.length; i++) {
            parameter = parameters[i];
            if (parameter.getName().equals(name)) break;
        }
        if (parameter == null) {
            throw new CommandExecutionException("Parameter '" + name + "' not found");
        }
        if (i >= callParams.length) {
            if (parameter.isOptional()) {
                return parameter.getDefaultValue();
            } else {
                throw new CommandExecutionException("Parameter not present in call parameters and is not optional");
            }
        }
        return callParams[i];
    }

    public String getName() {
        return name;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Represents a method parameter.
     */
    public static class Parameter {
        private String name;
        private boolean optional;
        /**
         * Only used if parameter can be optional.
         */
        private Value defaultValue;

        public Parameter(String name) {
            this.name = name;
        }

        public Parameter(String name, boolean optional, Value defaultValue) {
            this.name = name;
            this.optional = optional;
            this.defaultValue = defaultValue;
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
    }
}

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

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.base.BaseCommand;
import com.github.srec.command.base.CommandSymbol;
import com.github.srec.command.exception.IllegalParametersException;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a method (not a method call).
 * 
 * @author Victor Tatai
 */
public abstract class MethodCommand extends BaseCommand implements CommandSymbol {
    protected Map<String, MethodParameter> parameters = new HashMap<String, MethodParameter>();

    protected MethodCommand(String name, MethodParameter... parameters) {
        super(name);
        addParameters(parameters);
    }

    private void addParameters(MethodParameter[] parameters) {
        for (MethodParameter parameter : parameters) {
            this.parameters.put(parameter.getName(), parameter);
        }
    }

    protected MethodCommand(String name, String... parameters) {
        this(name, convertParameters(parameters));
    }

    private static MethodParameter[] convertParameters(String[] parameters) {
        MethodParameter[] ps = new MethodParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            ps[i] = new MethodParameter(parameters[i], Type.STRING);
        }
        return ps;
    }

    protected static MethodParameter param(String name) {
        return new MethodParameter(name, Type.STRING);
    }

    protected static MethodParameter param(String name, Type type) {
        return new MethodParameter(name, type);
    }

    protected static MethodParameter param(String name, Type type, boolean optional, Value value) {
        return new MethodParameter(name, type, optional, value);
    }

    /**
     * Executes the method call.
     *
     * @param context The execution context
     * @param params The parameters to run the method
     * @return The return value from the method call
     */
    public Value callMethod(ExecutionContext context, Map<String, Value> params) {
        validateParameters(params);
        fillDefaultValues(params);
        return internalCallMethod(context, params);
    }

    /**
     * Executes the method call after parameter validation.
     *
     * @param context The execution context
     * @param params The parameters to run the method
     * @return The return value from the method call
     */
    protected abstract Value internalCallMethod(ExecutionContext context, Map<String, Value> params);

    @Override
    public CommandFlow run(ExecutionContext context) {
        // a def command by definition does not run
        return CommandFlow.NEXT;
    }

    /**
     * Validates the parameters.
     *
     * @param callParameters The runtime (call) params
     */
    protected void validateParameters(Map<String, Value> callParameters) {
        for (String name : callParameters.keySet()) {
            if (!parameters.containsKey(name)) throw new IllegalParametersException("Parameter not supported: " + name);
        }
        for (Map.Entry<String, MethodParameter> entry : parameters.entrySet()) {
            if (entry.getValue().isOptional()) continue;
            if (!callParameters.containsKey(entry.getKey())) {
                throw new IllegalParametersException("Parameter not supplied: " + entry.getKey());
            }
        }
    }

    /**
     * Fills in the default values.
     *
     * @param params The parameters
     */
    protected void fillDefaultValues(Map<String, Value> params) {
        for (MethodParameter parameter : parameters.values()) {
            if (!parameter.isOptional() || parameter.getDefaultValue() == null || params.get(parameter.getName()) != null) continue;
            params.put(parameter.getName(), parameter.getDefaultValue());
        }
    }

    /**
     * Return true if the implementation for this command is native, ie, implemented directly in Java.
     *
     * @return true if native, false otherwise
     */
    public boolean isNative() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Map<String, MethodParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, MethodParameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(MethodParameter parameter) {
        this.parameters.put(parameter.getName(), parameter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITIES METHODS                                                                                              //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Gets a parameter value as a Java String.
     *
     * @param name The parameter name
     * @param params The parameters
     * @param context The EC
     * @return The String
     */
    protected String asString(String name, Map<String, Value> params, ExecutionContext context) {
        return coerceToString(params.get(name), context);
    }

    /**
     * Gets a parameter value as a Java Boolean.
     *
     * @param name The parameter name
     * @param params The parameters
     * @return The boolean
     */
    protected Boolean asBoolean(String name, Map<String, Value> params) {
        return coerceToBoolean(params.get(name));
    }

    /**
     * Gets a parameter value as a Java Boolean.
     *
     * @param name The parameter name
     * @param params The parameters
     * @return The boolean
     */
    protected BigDecimal asBigDecimal(String name, Map<String, Value> params) {
        return coerceToBigDecimal(params.get(name));
    }
    
    /**
     * Shortcut for creating an array of parameters. The values passed should be the parameter name and the parameter
     * type, such as "text", Type.STRING, "index", Type.NUMBER. If only one item is passed it is assumed to be of type
     * STRING.
     *
     * @param params The array of parameter names and types
     * @return The array of created MethodParameters
     */
    protected static MethodParameter[] params(Object... params) {
        if (params.length == 0) return null;
        if (params.length != 1 && params.length % 2 != 0) throw new IllegalParametersException("Incorrect number of params");
        if (params.length == 1) {
            MethodParameter[] ret = new MethodParameter[1];
            ret[0] = new MethodParameter(params[0].toString(), Type.STRING);
            return ret;
        }
        MethodParameter[] ret = new MethodParameter[params.length / 2];
        for (int i = 0; i < ret.length; i++) {
            String name = (String) params[i * 2];
            Type type = (Type) params[i * 2 + 1];
            ret[i] = new MethodParameter(name, type);
        }
        return ret;
    }
}

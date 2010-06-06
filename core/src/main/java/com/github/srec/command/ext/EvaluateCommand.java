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
import com.github.srec.command.base.VarCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.value.NilValue;
import com.github.srec.command.value.ObjectValue;
import com.github.srec.command.value.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * An evaluate command evaluates Java methods using introspection. Parameters are not yet supported.
 *
 * @author Victor Tatai
 */
@SRecCommand
public class EvaluateCommand extends MethodCommand {
    public static final String METHOD_PARAM = "method";
    public static final String VAR_NAME_PARAM = "varName";
    public static final String RESULT_VAR_NAME_PARAM = "resultVarName";

    public EvaluateCommand() {
        super("evaluate", METHOD_PARAM, VAR_NAME_PARAM, RESULT_VAR_NAME_PARAM);
    }

    @Override
    protected Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        String method = asString(METHOD_PARAM, params, context);
        String varName = asString(VAR_NAME_PARAM, params, context);
        String resultVarName = asString(RESULT_VAR_NAME_PARAM, params, context);
        Object obj = findObjectSymbol(varName, context);
        try {
            Method m = obj.getClass().getMethod(method);
            Object ret = m.invoke(obj);
            context.addSymbol(new VarCommand(resultVarName, location, ret == null ? NilValue.getInstance() : new ObjectValue(ret)));
        } catch (NoSuchMethodException e) {
            throw new CommandExecutionException(e);
        } catch (InvocationTargetException e) {
            throw new CommandExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new CommandExecutionException(e);
        }
        return null;
    }

    @Override
    public boolean isNative() {
        return true;
    }

    @Override
    public String toString() {
        return "evaluate";
    }
}
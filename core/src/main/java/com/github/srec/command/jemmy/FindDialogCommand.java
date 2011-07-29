/*
 * Copyright 2010 Victor Tatai Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.base.CommandSymbol;
import com.github.srec.command.base.VarCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NilValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.jemmy.JemmyDSL.Dialog;

import org.netbeans.jemmy.TimeoutExpiredException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.dialog;

/**
 * Finds a component assigning an id to it. This id can later be used as a locator in the form
 * "id=XXX". Notice that if the component is not found no error is thrown, and the id is assigned a
 * null value.
 * 
 * @author Victor Tatai
 */
@SRecCommand
public class FindDialogCommand extends JemmyEventCommand {
    public FindDialogCommand() {
        super("find_dialog",  new MethodParameter[] {new MethodParameter("title", Type.STRING),
                new MethodParameter("id", Type.STRING),
                new MethodParameter("required", Type.BOOLEAN, true, new BooleanValue(true))});
    }
    @Override
    public void runJemmy(ExecutionContext ctx, Map<String, Value> params) {
        String title = coerceToString(params.get("title"), ctx);
        String id = coerceToString(params.get("id"), ctx);
        Boolean required = coerceToBoolean(params.get("required"));
        if (required == null) {
            required = true;
        }
        try {
            Dialog dialog = dialog(title);
            dialog.activate().store(id);
        } catch (TimeoutExpiredException e) {
            if (required) {
                throw e;
            } else {
                VarCommand var = new VarCommand(id, NilValue.getInstance());
                ctx.addSymbol(var);
            }
        }
    }
}

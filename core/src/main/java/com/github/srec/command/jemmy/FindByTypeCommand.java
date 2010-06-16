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

package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.findByComponentType;

/**
 * Finds a component by its type, assigning an id to it. This id can later be used as a locator in the form "id=XXX".
 * Notice that if the component is not found no error is thrown, and the id is assigned a null value.
 *
 * @author Victor Tatai
 */
@SRecCommand
public class FindByTypeCommand extends JemmyEventCommand {
    public FindByTypeCommand() {
        super("find_by_type", param("id", Type.STRING), param("containerId", Type.STRING, true, null), param("findComponentType", Type.STRING));
    }

    @Override
    public void runJemmy(ExecutionContext ctx, Map<String, Value> params) {
        findByComponentType(asString("id", params, ctx), asString("containerId", params, ctx), asString("findComponentType", params, ctx));
    }
}
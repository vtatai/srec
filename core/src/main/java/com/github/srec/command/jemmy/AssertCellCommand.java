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
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.table;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertCellCommand extends JemmyEventCommand {
    public AssertCellCommand() {
        super("assert_cell", createParametersDefinition("table", Type.STRING, "row", Type.NUMBER, "text", Type.STRING,
                "column", Type.NUMBER));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        table(coerceToString(params.get("table"), ctx))
                .row(coerceToBigDecimal(params.get("row")).intValue())
                .assertColumn(coerceToBigDecimal(params.get("column")).intValue(), coerceToString(params.get("text"), ctx));
    }
}
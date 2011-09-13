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
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.table;

/**
 * @author Wilian C. B.
 */
@SRecCommand
public class ClickHeaderCommand extends JemmyEventCommand {
    public static final String COUNT = "count";
    public static final String COLUMN = "column";
    
    public ClickHeaderCommand() {
        super("click_header", 
              param(LOCATOR),
              param(COLUMN, Type.NUMBER, false, null),
              param(COUNT, Type.NUMBER, true, new NumberValue("1")));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        table(coerceToString(params.get(LOCATOR), ctx))
                .header()
                .click(coerceToBigDecimal(params.get(COLUMN)).intValue(), asBigDecimal(COUNT, params).intValue());
    }
}
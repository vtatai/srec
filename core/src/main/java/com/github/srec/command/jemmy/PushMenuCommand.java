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
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.menuBar;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class PushMenuCommand extends JemmyEventCommand {
    public static final String INDEXES = "indexes";
    public static final String PATH = "path";

    public PushMenuCommand() {
        super("push_menu", param(INDEXES, Type.STRING, true, null), param(PATH, Type.STRING, true, null));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.get(INDEXES) != null) {
            String indexesStr = coerceToString(params.get(INDEXES), ctx);
            String[] indexesArray = indexesStr.split("[ |,]+");
            menuBar().clickMenu(convertToInt(indexesArray));
        } else if (params.get(PATH) != null) {
            String pathStr = coerceToString(params.get(PATH), ctx);
            String[] pathArray = pathStr.split("[ |>]+");
            menuBar().clickMenu(pathArray);
        } else {
            throw new CommandExecutionException("Either indexes or path must be specified for push_menu");
        }
    }

    private int[] convertToInt(String[] strings) {
        int[] ret = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ret[i] = Integer.parseInt(strings[i]);
        }
        return ret;
    }
}

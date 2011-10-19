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

import static com.github.srec.jemmy.JemmyDSL.tabbedPane;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class TabCommand extends JemmyEventCommand {
    public TabCommand() {
        super("tab", param(LOCATOR, Type.STRING), 
        		     param("text", Type.STRING, true, null),
        		     param("index", Type.STRING, true, null));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
    	String locator = coerceToString(params.get(LOCATOR), ctx);
    	String text = coerceToString(params.get("text"), ctx);
    	String indexStr = coerceToString(params.get("index"), ctx);
    	
    	if (text != null) {
    		tabbedPane(locator).select(text);
    	} else if (indexStr != null) {
    		tabbedPane(locator).select(Integer.parseInt(indexStr));
    	} else {
    		throw new CommandExecutionException("Attribute text or index must be defined in <tab> tag.");
    	}
    }
}
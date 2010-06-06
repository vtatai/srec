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

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.awt.event.KeyEvent;
import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class TypeSpecialCommand extends JemmyEventCommand {
    public TypeSpecialCommand() {
        super("type_special", createParametersDefinition(LOCATOR, Type.STRING, "text", Type.STRING));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        int key;
        String keyString = coerceToString(params.get("text"), ctx);
        if ("Tab".equalsIgnoreCase(keyString)) key = KeyEvent.VK_TAB;
        else if ("End".equalsIgnoreCase(keyString)) key = KeyEvent.VK_END;
        else if ("Backspace".equalsIgnoreCase(keyString)) key = KeyEvent.VK_BACK_SPACE;
        else throw new UnsupportedFeatureException("Type special for " + params.get("text") + " not supported");
        textField(coerceToString(params.get(LOCATOR), ctx)).type(key);
    }
}

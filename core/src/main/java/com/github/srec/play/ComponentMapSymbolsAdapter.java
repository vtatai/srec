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

package com.github.srec.play;

import com.github.srec.command.base.CommandSymbol;
import com.github.srec.command.base.VarCommand;
import com.github.srec.command.value.ObjectValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.ComponentMap;
import org.netbeans.jemmy.operators.ComponentOperator;

import java.util.Map;

/**
 * @author Victor Tatai
 */
public class ComponentMapSymbolsAdapter implements ComponentMap {
    private Map<String, CommandSymbol> symbols;

    public ComponentMapSymbolsAdapter(Map<String, CommandSymbol> symbols) {
        this.symbols = symbols;
        if (symbols == null) throw new PlayerException("symbols cannot be null");
    }

    @Override
    public ComponentOperator getComponent(String id) {
        CommandSymbol s = symbols.get(id);
        if (s == null) return null;
        if (!(s instanceof VarCommand)) throw new PlayerException("Symbol cannot be converted to a ComponentOperator because it is not a variable");
        Value value = ((VarCommand) s).getValue(null);
        if (value == null || value.getType() == Type.NIL) return null;
        if (value.getType() != Type.OBJECT) throw new PlayerException("Variable value cannot be converted to a ComponentOperator");
        Object object = value.get();
        if (object == null) return null;
        if (!(object instanceof ComponentOperator)) throw new PlayerException("Variable value cannot be converted to a ComponentOperator");
        return (ComponentOperator) object;
    }

    @Override
    public void putComponent(String id, ComponentOperator component) {
        symbols.put(id, new VarCommand(id, new ObjectValue(component)));
    }
}

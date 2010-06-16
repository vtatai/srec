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

package com.github.srec.jemmy;

import org.netbeans.jemmy.operators.ComponentOperator;

import java.util.HashMap;

/**
 * @author Victor Tatai
 */
public class DefaultComponentMap extends HashMap<String, ComponentOperator> implements ComponentMap {
    @Override
    public ComponentOperator getComponent(String id) {
        return get(id);
    }

    @Override
    public void putComponent(String id, ComponentOperator component) {
        put(id, component);
    }
}

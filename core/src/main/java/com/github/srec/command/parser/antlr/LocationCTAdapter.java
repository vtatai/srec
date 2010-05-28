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

package com.github.srec.command.parser.antlr;

import com.github.srec.Location;
import org.antlr.runtime.tree.CommonTree;

/**
 * @author Victor Tatai
 */
public class LocationCTAdapter extends Location {
    private CommonTree tree;

    public LocationCTAdapter(String fileName, CommonTree tree) {
        super(fileName, tree.getLine(), tree.getCharPositionInLine(), tree.toString());
        this.tree = tree;
    }

    public CommonTree getTree() {
        return tree;
    }
}

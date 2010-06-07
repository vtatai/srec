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

package com.github.srec.util;

import java.awt.*;

/**
 * Scans a tree of components for the one component which matches a given condition.
 *
 * @author Victor Tatai
 */
public class AWTTreeScanner {

    /**
     * Scans a component tree searching for the one which matches the given name.
     *
     * @param root The component root
     * @param matcher The matcher
     * @return The component found
     */
    public static Component scan(Component root, ScannerMatcher matcher) {
        if (matcher.matches(root)) return root;
        if (!(root instanceof Container)) return null;
        Container container = (Container) root;
        for (Component component : container.getComponents()) {
            Component found = scan(component, matcher);
            if (found != null) return found;
        }
        return null;
    }

    /**
     * Matches by component name.
     */
    public static class NameScannerMatcher implements ScannerMatcher {
        private String name;

        public NameScannerMatcher(String name) {
            this.name = name;
        }

        @Override
        public boolean matches(Component component) {
            return component != null && component.getName().equals(name);
        }
    }
}

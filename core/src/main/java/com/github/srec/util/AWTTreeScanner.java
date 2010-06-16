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

import com.github.srec.SRecException;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Scans a tree of components for the one component which matches a given condition.
 *
 * @author Victor Tatai
 */
public class AWTTreeScanner {

    /**
     * Scans a component tree searching for the one which is matched by the given matcher. In case of multiple matches
     * the firs one found is returned.
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
            return component != null && component.getName() != null && component.getName().equals(name);
        }
    }

    /**
     * Matches by component name.
     */
    public static class TitleScannerMatcher implements ScannerMatcher {
        private String title;

        public TitleScannerMatcher(String title) {
            this.title = title;
        }

        @Override
        public boolean matches(Component component) {
            if (component == null || !(component instanceof JDialog || component instanceof JFrame || component instanceof JInternalFrame))
                return false;
            if (component instanceof JDialog) return ((JDialog) component).getTitle().equals(title);
            if (component instanceof JFrame) return ((JFrame) component).getTitle().equals(title);
            return ((JInternalFrame) component).getTitle().equals(title);
        }
    }

    /**
     * Matches by component name.
     */
    public static class TextScannerMatcher implements ScannerMatcher {
        private String text;

        public TextScannerMatcher(String text) {
            this.text = text;
        }

        @Override
        public boolean matches(Component component) {
            if (component == null) return false;
            Method m = findGetTextMethod(component.getClass());
            if (m == null) return false;
            try {
                String currText = (String) m.invoke(component);
                return text.equals(currText);
            } catch (IllegalAccessException e) {
                throw new SRecException(e);
            } catch (InvocationTargetException e) {
                throw new SRecException(e);
            }
        }

        private Method findGetTextMethod(Class<? extends Component> clazz) {
            for (Method method : clazz.getMethods()) {
                if ("getText".equals(method.getName()) && method.getParameterTypes().length == 0) return method;
            }
            return null;
        }
    }
}

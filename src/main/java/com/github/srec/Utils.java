package com.github.srec;

import com.github.srec.ui.LaunchDialog;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class Utils {
    private Utils() {}

    public static String quote(String str) {
        if (str == null) return null;
        return "\"" + str + "\"";
    }

    public static String unquote(String str) {
        if (StringUtils.isBlank(str)) return str;
        final int first = str.indexOf('"');
        final int last = str.lastIndexOf('"');
        if (first == -1 || first == last) return str;
        return str.substring(first + 1, last);
    }

    public static JLabel getLabelFor(Container container, Component component) {
        for (int i = 0; i < container.getComponents().length; i++) {
            Component curr = container.getComponents()[i];
            if (curr instanceof JLabel && ((JLabel) curr).getLabelFor() == component) return (JLabel) curr;
            if (curr instanceof Container) {
                JLabel l = getLabelFor((Container) curr, component);
                if (l != null) return l;
            }
        }
        return null;
    }
    
    public static Map<String, String> parseLocator(String locatorString) {
        Map<String, String> map = new HashMap<String, String>();
        if (locatorString.indexOf('=') == -1) {
            map.put("name", locatorString);
            return map;
        }
        String[] strs = locatorString.split("=");
        for (int i = 0; i < strs.length; i = i + 2) {
            String key = strs[i].trim();
            String value;
            if (i + 1 >= strs.length) value = "";
            else value = strs[i + 1].trim();
            map.put(key, value);
        }
        return map;
    }

    public static void runMain(String clName, String[] args) {
        try {
            Class cl = Class.forName(clName);
            Method m = cl.getMethod("main", String[].class);
            if (!Modifier.isStatic(m.getModifiers())) {
                throw new MainMethodRunningException("Incorrect signature for main method");
            }
            if (!m.getReturnType().equals(void.class)) {
                throw new MainMethodRunningException("Incorrect signature for main method");
            }
            m.invoke(null, new Object[]{ args });
        } catch (ClassNotFoundException e1) {
            throw new MainMethodRunningException("Incorrect signature for main method");
        } catch (NoSuchMethodException e1) {
            throw new MainMethodRunningException("Incorrect signature for main method");
        } catch (InvocationTargetException e1) {
            throw new MainMethodRunningException("Incorrect signature for main method");
        } catch (IllegalAccessException e1) {
            throw new MainMethodRunningException("Incorrect signature for main method");
        }
    }
}

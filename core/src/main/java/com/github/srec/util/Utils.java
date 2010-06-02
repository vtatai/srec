package com.github.srec.util;

import com.github.srec.MainMethodRunningException;
import com.github.srec.SRecException;
import com.github.srec.command.*;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.*;
import com.github.srec.command.value.StringValue;
import groovy.lang.Binding;
import groovy.lang.GString;
import groovy.lang.GroovyShell;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author Victor Tatai
 */
public final class Utils {
    /**
     * Flag which indicates whether the recorder should scan for labels if the text field has no name.
     */
    private static final boolean SCAN_FOR_LABELS = true;

    private Utils() {}

    public static String quote(String str) {
        if (str == null) return null;
        return "\"" + str + "\"";
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

    /**
     * Close all windows.
     *
     * @param ignoredWindows The windows to ignore
     */
    public static void closeWindows(Window... ignoredWindows) {
        Window[] ws = Window.getWindows();
        for (Window w : ws) {
            if (!contains(ignoredWindows, w)) w.dispose();
        }
    }

    private static boolean contains(Window[] windows, Window w) {
        for (Window window : windows) {
            if (window == w) return true;
        }
        return false;
    }

    public static String getLocator(Component component) {
        String locator = component.getName();
        if (isBlank(locator) && SCAN_FOR_LABELS) {
            JLabel label = getLabelFor(component.getParent(), component);
            if (label != null) locator = "label=" + label.getText();
        }
        return locator;
    }

    private static int findComponentIndex(Component component) {
        for (int i = 0; i < component.getParent().getComponents().length; i++) {
            Component candidate = component.getParent().getComponents()[i];
            if (candidate == component) return i;
        }
        throw new SRecException("Could not find component inside its parent");
    }

    public static String asString(Map<String, Value> parameters) {
        if (parameters == null) return "";
        StringBuilder strb = new StringBuilder();
        for (Map.Entry<String, Value> entry : parameters.entrySet()) {
            String parameter = entry.getValue().toString();
            strb.append(parameter).append(", ");
        }
        final String s = strb.toString();
        return s.endsWith(", ") ? s.substring(0, s.length() - 2) : s;
    }

    /**
     * Trims all elements inside the array, modifying the original array.
     *
     * @param strings The string array
     * @return The passed string array, trimmed
     */
    public static String[] trimArray(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
        }
        return strings;
    }

    /**
     * Utility method useful for creating a parameter map.
     *
     * @param params The parameters, should be in the format "name", value
     * @return The parameters map
     */
    public static Map<String, ValueCommand> createParameterMap(Object... params) {
        assert params.length % 2 == 0;
        Map<String, ValueCommand> ret = new HashMap<String, ValueCommand>();
        for (int i = 0; i < params.length; i = i + 2) {
            String param = params[i].toString();
            Value value = convertFromJava(params[i + 1]);
            ret.put(param, new LiteralCommand(value));
        }
        return ret;
    }

    /**
     * Converts a Java object to a srec value.
     *
     * @param o The Java object
     * @return The srec value
     */
    public static Value convertFromJava(Object o) {
        if (o instanceof Long) {
            return new NumberValue(new BigDecimal((Long) o));
        } else if (o instanceof Integer) {
            return new NumberValue(new BigDecimal((Integer) o));
        } else if (o instanceof Double) {
            return new NumberValue(new BigDecimal((Double) o));
        } else if (o instanceof Float) {
            return new NumberValue(new BigDecimal((Float) o));
        } else if (o instanceof String) {
            return new StringValue((String) o);
        } else if (o instanceof Date) {
            return new DateValue((Date) o);
        } else if (o instanceof Boolean) {
            return BooleanValue.getInstance((Boolean) o);
        } else if (o == null) {
            return NilValue.getInstance();
        }
        throw new CommandExecutionException("Could not convert Java object " + o + " to an equivalent srec value");
    }

    /**
     * Converts a srec value to a Java object.
     *
     * @param value The value
     * @return The Java object
     */
    public static Object convertToJava(Value value) {
        switch (value.getType()) {
            case STRING:
                return ((StringValue) value).get();
            case BOOLEAN:
                return ((BooleanValue) value).get();
            case NUMBER:
                return ((NumberValue) value).get();
            case DATE:
                return ((DateValue) value).get();
            case NIL:
                return null;
        }
        throw new CommandExecutionException("Could not convert value " + value + " to an equivalent Java object");
    }

    /**
     * Evaluates an expression using Groovy. All VarCommands inside the context are used in order to evaluate the given
     * expression.
     *
     * @param context The EC
     * @param expression The expression to evaluate
     * @return The value
     */
    public static Object groovyEvaluate(ExecutionContext context, String expression) {
        Binding binding = new Binding();
        for (Map.Entry<String, CommandSymbol> entry : context.getSymbols().entrySet()) {
            final CommandSymbol symbol = entry.getValue();
            if (symbol instanceof VarCommand) {
                binding.setVariable(entry.getKey(), convertToJava(((VarCommand) symbol).getValue(context)));
            }
        }
        GroovyShell shell = new GroovyShell(binding);
        final Object o = shell.evaluate(expression);
        if (o instanceof GString) {
            return o.toString();
        }
        return o;
    }

    /**
     * Evaluates an expression using Groovy, converting the final value.
     *
     * @param context The EC
     * @param expression The expression to evaluate
     * @return The value converted
     */
    public static Value groovyEvaluateConvert(ExecutionContext context, String expression) {
        Object obj = groovyEvaluate(context, expression);
        return Utils.convertFromJava(obj);
    }
}

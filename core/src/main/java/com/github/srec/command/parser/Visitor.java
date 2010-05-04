package com.github.srec.command.parser;

import com.github.srec.command.*;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor for a srec script tree parsed by ANTLR.
 *
 * @author Victor Tatai
 */
public class Visitor {
    private static final Logger logger = Logger.getLogger(Visitor.class);
    private ExecutionContext context;
    private MethodScriptCommand currentMethod;
    private List<ParseError> errors = new ArrayList<ParseError>();

    public Visitor(ExecutionContext initialContext) {
        this.context = initialContext;
    }

    public void visit(CommonTree t, CommonTree parent) {
        if (t == null || t.getChildren() == null) return;
        for (Object o : t.getChildren()) {
            if (!(o instanceof CommonTree)) throw new IllegalStateException();
            CommonTree child = (CommonTree) o;
            String commandName = child.getText();
            if (!StringUtils.isBlank(commandName)) logger.debug("Processing: " + commandName);
            if (commandName.equals("METHOD_CALL")) {
                visitMETHOD_CALL(child, t);
            } else if (commandName.equals("METHOD_CALL_OR_VARREF")) {
                // VARREFs are distinguished from method calls during runtime, there is no way of telling them apart here
                visitMETHOD_CALL(child, t);
            } else if (commandName.equals("METHOD_DEF")) {
                visitMETHOD_DEF(child, t);
            } else if (commandName.equals("METHOD_DEF_PARAMS")) {
                visitMETHOD_DEF_PARAMS(child, t);
            } else if (commandName.equals("METHOD_BODY")) {
                visit(child, t);
            } else if (StringUtils.isBlank(commandName)) {
            } else {
                throw new UnsupportedCommandException(commandName);
            }
        }
    }

    public void visitMETHOD_CALL(CommonTree t, CommonTree parent) {
        add(new CallCommand(getChildText(t, 0), t, extractMethodParams(t)));
    }

    private static String getChildText(CommonTree parent, int index) {
        return parent.getChild(index).getText();
    }

    private String[] extractMethodParams(CommonTree t) {
        String[] params = new String[t.getChildCount() - 1];
        for (int i = 1; i < t.getChildCount(); i++) {
            CommonTree child = (CommonTree) t.getChild(i);
            params[i - 1] = child.getChild(0).getText(); // Child is LITNUMBER, etc
        }
        return params;
    }

    private void visitMETHOD_DEF(CommonTree t, CommonTree parent) {
        if (currentMethod != null) {
            error(t, "Nested methods are not allowed");
            return;
        }
        MethodScriptCommand method = new MethodScriptCommand(getChildText(t, 0), extractMethodDefParameters((CommonTree) t.getChild(1)));
        add(method);
        currentMethod = method;
        visit((CommonTree) t.getChild(1), t);
        visit((CommonTree) t.getChild(2), t);
        currentMethod = null;
    }

    private void visitMETHOD_DEF_PARAMS(CommonTree t, CommonTree parent) {
        if (currentMethod == null) {
            throw new SRecParseException("Current method is null when parsing params - should never happen!");
        }
        String[] params = new String[t.getChildCount() - 1];
        for (int i = 0; i < params.length; i++) {
            params[i] = t.getChild(i + 1).getText();
        }
        currentMethod.setParameters(params);
    }

    private String[] extractMethodDefParameters(CommonTree t) {
        if (t == null) return new String[0];
        String[] params = new String[t.getChildCount()];
        for (int i = 0; i < t.getChildCount(); i++) {
            CommonTree child = (CommonTree) t.getChild(i);
            params[i] = child.getText();
        }
        return params;
    }

    private void error(CommonTree t, String message) {
        errors.add(new ParseError(ParseError.Severity.ERROR, t, message));
    }

    private void add(Command command) {
        if (currentMethod == null) context.addCommand(command);
        else currentMethod.add(command);
    }

    public ExecutionContext getContext() {
        return context;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}

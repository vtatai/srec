package com.github.srec.command.parser;

import com.github.srec.command.*;
import com.github.srec.command.exception.UnsupportedCommandException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private Stack<CallCommand> callStack = new Stack<CallCommand>();  

    public Visitor(ExecutionContext initialContext) {
        this.context = initialContext;
    }

    public void visit(CommonTree t, CommonTree parent) throws IOException, RecognitionException {
        visit(t, parent, 0);
    }

    public void visit(CommonTree t, CommonTree parent, int index) throws IOException, RecognitionException {
        if (t == null || t.getChildren() == null) return;
        for (int i = index; i < t.getChildCount(); i++) {
            Object o = t.getChild(i);
            if (!(o instanceof CommonTree)) throw new IllegalStateException();
            CommonTree child = (CommonTree) o;
            String commandName = child.getText();
            if (!StringUtils.isBlank(commandName)) logger.debug("Processing: " + commandName);
            if (commandName.equals("METHOD_CALL")) {
                visitMETHOD_CALL(child, t);
            } else if (commandName.equals("METHOD_CALL_OR_VARREF")) {
                visitMETHOD_CALL_OR_VARREF(child, t);
            } else if (commandName.equals("METHOD_DEF")) {
                visitMETHOD_DEF(child, t);
            } else if (commandName.equals("METHOD_DEF_PARAMS")) {
                visitMETHOD_DEF_PARAMS(child, t);
            } else if (commandName.equals("METHOD_BODY")) {
                visit(child, t);
            } else if (commandName.equals("REQUIRE")) {
                visitREQUIRE(child, t);
            } else if (commandName.equals("LITNUMBER")) {
                visitLITNUMBER(child, t);
            } else if (commandName.equals("LITSTRING")) {
                visitLITSTRING(child, t);
            } else if (commandName.equals("LITBOOLEAN")) {
                visitLITBOOLEAN(child, t);
            } else if (commandName.equals("LITNIL")) {
                visitLITNIL(child, t);
            } else if (StringUtils.isBlank(commandName)) {
            } else {
                throw new UnsupportedCommandException(commandName);
            }
        }
    }

    public void visitMETHOD_CALL(CommonTree t, CommonTree parent) throws IOException, RecognitionException {
        CallCommand command = new CallCommand(getChildText(t, 0), t);
        add(command);
        callStack.push(command);
        visit(t, parent, 1);
        callStack.pop();
    }

    public void visitMETHOD_CALL_OR_VARREF(CommonTree t, CommonTree parent) {
        String name = getChildText(t, 0);
        CommandSymbol s = context.findSymbol(name);
        if (s instanceof MethodCommand) {
            add(new CallCommand(name, t));
        } else if (s instanceof VarCommand) {
            add(s);
        } else {
            error(t, "Could not resolve reference '" + name + "' to either a method or var");
        }
    }

    private static String getChildText(CommonTree parent, int index) {
        return parent.getChild(index).getText();
    }

    private void visitMETHOD_DEF(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        if (currentMethod != null) {
            error(t, "Nested methods are not allowed");
            return;
        }
        MethodScriptCommand method = new MethodScriptCommand(getChildText(t, 0), extractMethodDefParameters((CommonTree) t.getChild(1)));
        currentMethod = method;
        context = new NestedExecutionContext(context, context.getFile());
        visit(t, parent, 1);
        context = ((NestedExecutionContext) context).getParent();
        currentMethod = null;
        context.addSymbol(method);
    }

    private void visitMETHOD_DEF_PARAMS(CommonTree t, CommonTree parent) {
        if (currentMethod == null) {
            throw new ParseException("Current method is null when parsing params - should never happen!");
        }
        String[] params = new String[t.getChildCount()];
        for (int i = 0; i < params.length; i++) {
            params[i] = t.getChild(i).getText();
            context.addSymbol(new VarCommand(params[i], (CommonTree) t.getChild(i), null));
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

    private void visitREQUIRE(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        ExecutionContext newContext = new ExecutionContext(context.getPlayer(), context.getFile());
        ScriptParser parser = new ScriptParser();
        parser.parse(newContext, locateRequiredFile(t.getChild(0).getText()));
        merge(context, newContext);
        errors.addAll(parser.getErrors());
    }

    private File locateRequiredFile(String name) throws IOException {
        for (String path : context.getLoadPath()) {
            File f = new File(path + File.separator + name);
            if (f.exists()) return f;
        }
        return null;
    }

    private void merge(ExecutionContext context, ExecutionContext newContext) {
        context.addAllSymbols(newContext);
    }

    private void visitLITNUMBER(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand((CommonTree) t.getChild(0), t.getChild(0).getText()));
    }

    private void visitLITSTRING(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand((CommonTree) t.getChild(0), t.getChild(0).getText()));
    }

    private void visitLITBOOLEAN(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand((CommonTree) t.getChild(0), t.getChild(0).getText()));
    }

    private void visitLITNIL(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand(null, t.getChild(0).getText()));
    }

    private void error(CommonTree t, String message) {
        try {
            errors.add(new ParseError(ParseError.Severity.ERROR, context.getFile().getCanonicalPath(), t, message));
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    private void add(Command command) {
        if (command instanceof ValueCommand && !callStack.isEmpty()) {
            CallCommand call = callStack.peek();
            call.addParameter((ValueCommand) command);
            return;
        }
        if (currentMethod == null) {
            context.addCommand(command);
        } else {
            currentMethod.add(command);
        }
    }

    public ExecutionContext getContext() {
        return context;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}

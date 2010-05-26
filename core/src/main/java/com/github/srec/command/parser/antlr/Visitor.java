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

import com.github.srec.command.*;
import com.github.srec.command.exception.UnsupportedCommandException;
import com.github.srec.command.method.MethodCallCommand;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.method.MethodScriptCommand;
import com.github.srec.command.parser.ParseError;
import com.github.srec.command.parser.ParseException;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NilValue;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.StringValue;
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
    private Stack<MethodCallCommand> callStack = new Stack<MethodCallCommand>();

    public Visitor(ExecutionContext initialContext) {
        this.context = initialContext;
    }

    public void visit(CommonTree t, CommonTree parent) throws IOException, RecognitionException {
        visit(t, parent, 0);
    }

    @SuppressWarnings({"UnusedDeclaration"})
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
        String name = getChildText(t, 0);
        MethodCallCommand command = new MethodCallCommand(name, new ParseLocationCTAdapter(context.getFile().getCanonicalPath(), t));
        CommandSymbol s = context.findSymbol(name);
        if (s == null) {
            error(t, "Could not resolve reference '" + name + "' to a method");
            return;
        } else if (!(s instanceof MethodCommand)) {
            error(t, "Reference '" + name + "' does not refer to a method");
            return;
        }
        add(command);
        callStack.push(command);
        visit(t, parent, 1);
        callStack.pop();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void visitMETHOD_CALL_OR_VARREF(CommonTree t, CommonTree parent) throws IOException {
        String name = getChildText(t, 0);
        CommandSymbol s = context.findSymbol(name);
        if (s instanceof MethodCommand) {
            add(new MethodCallCommand(name, new ParseLocationCTAdapter(context.getFile().getCanonicalPath(), t)));
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
        MethodScriptCommand method = new MethodScriptCommand(getChildText(t, 0), context.getFile(),
                extractMethodDefParameters((CommonTree) t.getChild(1)));
        currentMethod = method;
        context = new NestedExecutionContext(context, context.getFile());
        visit(t, parent, 1);
        context = ((NestedExecutionContext) context).getParent();
        currentMethod = null;
        context.addSymbol(method);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitMETHOD_DEF_PARAMS(CommonTree t, CommonTree parent) throws IOException {
        throw new UnsupportedOperationException();
//        if (currentMethod == null) {
//            throw new ParseException("Current method is null when parsing params - should never happen!");
//        }
//        Map<String, MethodCommand.Parameter> params = new HashMap<String, MethodCommand.Parameter>(t.getChildCount());
//        for (int i = 0; i < t.getChildCount(); i++) {
//            params.put("", new MethodCommand.Parameter(t.getChild(i).getText()));
//            context.addSymbol(new VarCommand(params[i].getName(), new ParseLocationCTAdapter(context.getCanonicalPath(),
//                    (CommonTree) t.getChild(i)), null));
//        }
//        currentMethod.setParameters(params);
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

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitREQUIRE(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        File file = locateRequiredFile(t.getChild(0).getText());
        NestedExecutionContext newContext = new NestedExecutionContext(context, file);
        ScriptParser parser = new ScriptParser();
        parser.parse(newContext, file);
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

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitLITNUMBER(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand(new ParseLocationCTAdapter(context.getCanonicalPath(), (CommonTree) t.getChild(0)),
                new NumberValue(t.getChild(0).getText())));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitLITSTRING(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand(new ParseLocationCTAdapter(context.getCanonicalPath(), (CommonTree) t.getChild(0)),
                new StringValue(t.getChild(0).getText())));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitLITBOOLEAN(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand(new ParseLocationCTAdapter(context.getCanonicalPath(), (CommonTree) t.getChild(0)),
                new BooleanValue(t.getChild(0).getText())));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void visitLITNIL(CommonTree t, CommonTree parent) throws RecognitionException, IOException {
        add(new LiteralCommand(new ParseLocationCTAdapter(context.getCanonicalPath(), (CommonTree) t.getChild(0)),
                new NilValue()));
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
            throw new UnsupportedOperationException();
//            MethodCallCommand call = callStack.peek();
//            call.addParameter((ValueCommand) command);
//            return;
        }
        if (currentMethod == null) {
            context.addCommand(command);
        } else {
            currentMethod.addCommand(command);
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ExecutionContext getContext() {
        return context;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}

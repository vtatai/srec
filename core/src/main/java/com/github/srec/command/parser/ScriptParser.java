package com.github.srec.command.parser;

import com.github.srec.command.ExecutionContext;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Parses a script using ANTLR.
 *
 * @author Victor Tatai
 */
public class ScriptParser {
    private Visitor visitor;

    public void parse(ExecutionContext context, File file) throws IOException {
        parse(context, new FileInputStream(file));
    }

    public void parse(ExecutionContext context, InputStream is) throws IOException {
        try {
            ANTLRInputStream input = new ANTLRInputStream(is);

            srecLexer lexer = new srecLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            srecParser parser = new srecParser(tokens);
            srecParser.script_return r = parser.script();

            CommonTree t = (CommonTree) r.getTree();

            visitor = new Visitor(context);
            visitor.visit(t, null);
        } catch (RecognitionException e) {
            throw new ParseException(e);
        }
    }

    public List<ParseError> getErrors() {
        return visitor.getErrors();
    }

    public static void main(String[] args) throws RecognitionException, IOException {
        new ScriptParser().parse(new ExecutionContext(null), new File(args[0]));
    }
}

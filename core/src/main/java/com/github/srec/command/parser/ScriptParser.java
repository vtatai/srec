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

/**
 * Parses a script using ANTLR.
 *
 * @author Victor Tatai
 */
public class ScriptParser {
    public static void parse(ExecutionContext context, File file) throws IOException, RecognitionException {
        parse(context, new FileInputStream(file));
    }

    public static void parse(ExecutionContext context, InputStream is) throws IOException, RecognitionException {
        ANTLRInputStream input = new ANTLRInputStream(is);

        srecLexer lexer = new srecLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        srecParser parser = new srecParser(tokens);
        srecParser.script_return r = parser.script();

        CommonTree t = (CommonTree) r.getTree();

        Visitor v = new Visitor(context);
        v.visit(t, null);
        v.getContext();
    }

    public static void main(String[] args) throws RecognitionException, IOException {
        ScriptParser.parse(new ExecutionContext(System.getProperty("user.dir")), new File(args[0]));
    }
}

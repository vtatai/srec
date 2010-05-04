package com.github.srec.command.parser;

import org.antlr.gunit.GrammarInfo;
import org.antlr.gunit.gUnitExecutor;
import org.antlr.gunit.gUnitLexer;
import org.antlr.gunit.gUnitParser;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Victor Tatai
 */
@Test
public class GrammarTest {
    public void test() throws Exception {
        gunit("srec");
    }

    protected void gunit(String name) throws IOException, ClassNotFoundException, RecognitionException {
        run("src/test/gunit/" + name + ".gunit");
    }

    protected static void run(String gunitFilename) throws IOException, ClassNotFoundException, RecognitionException {
        CharStream input = new ANTLRFileStream(gunitFilename);
        File f = new File(gunitFilename);
        String testsuiteDir = getTestsuiteDir(f.getCanonicalPath(), f.getName());
        gUnitExecutor executor = new gUnitExecutor(parse(input), testsuiteDir);
        System.out.print(executor.execTest()); // unit test result
        if ((executor.invalids.size() + executor.failures.size()) > 0) {
            Assert.fail("Grammar test failed (see log)");
        }
    }

    protected static GrammarInfo parse(CharStream input) throws RecognitionException {
        gUnitLexer lexer = new gUnitLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarInfo grammarInfo = new GrammarInfo();
        gUnitParser parser = new gUnitParser(tokens, grammarInfo);
        parser.gUnitDef(); // parse gunit script and save elements to grammarInfo
        return grammarInfo;
    }

    private static String getTestsuiteDir(String fullPath, String fileName) {
        return fullPath.substring(0, fullPath.length() - fileName.length());
    }
}

package com.github.srec.play;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Test
public class PlayerTest {
    private static final String TEST_SCRIPT_DIR = "src/test/resources/";

    public void test() throws IOException {
        runTest("test_form.xml");
    }

    public void testError() throws IOException {
        Player p = new Player()
                .init()
                .startAndPlay(new File(TEST_SCRIPT_DIR + "test_form_error.xml"), "com.github.srec.ui.TestForm", new String[0]);
        p.printErrors();
        assertEquals(p.getErrors().size(), 2);
        assertEquals(p.getErrors().get(0).getLineNumber(), 18);
        assertEquals(p.getErrors().get(1).getLineNumber(), 26);
    }

    public void testMethod() throws IOException {
        runTest("test_form_method_call.xml");
    }

    public void testMethodDeep() throws IOException {
        runTest("test_form_method_deep.xml");
    }

    public void testIf() throws IOException {
        runTest("if.xml");
    }

    public void testWhile() throws IOException {
        runTest("while.xml");
    }

    private void runTest(String script) {
        try {
            Player p = new Player()
                    .init()
                    .startAndPlay(new File(TEST_SCRIPT_DIR + script), "com.github.srec.ui.TestForm", new String[0]);
            p.printErrors();
            assertEquals(p.getErrors().size(), 0);
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }
}

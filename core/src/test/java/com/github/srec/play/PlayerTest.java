package com.github.srec.play;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

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
        assertNotNull(p.getError());
        assertEquals(p.getError().getLine(), 18);
    }

    public void testMethod() throws IOException {
        runTest("test_form_method_call.xml");
    }

    private void runTest(String script) {
        try {
            Player p = new Player()
                    .init()
                    .startAndPlay(new File(TEST_SCRIPT_DIR + script), "com.github.srec.ui.TestForm", new String[0]);
            if (p.getError() != null) p.getError().printStackTrace();
            assertNull(p.getError());
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }
}

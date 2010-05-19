package com.github.srec.play;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.fail;

@Test
public class PlayerTest {
    private static final String TEST_SCRIPT_DIR = "src/test/ruby/";

    public void test() throws IOException {
        try {
            new Player()
                    .init()
                    .startAndPlay(new File(TEST_SCRIPT_DIR + "test_form.rb"), "com.github.srec.ui.TestForm", new String[0]);
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }

    @Test(expectedExceptions = PlayerException.class)
    public void testError() throws IOException {
        new Player()
                .init()
                .startAndPlay(new File(TEST_SCRIPT_DIR + "test_form_error.rb"), "com.github.srec.ui.TestForm", new String[0]);
    }

    public void testMethod() throws IOException {
        new Player()
                .init()
                .startAndPlay(new File(TEST_SCRIPT_DIR + "test_form_method_call.rb"), "com.github.srec.ui.TestForm", new String[0]);
    }

}

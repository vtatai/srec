package com.github.srec.play;

import com.github.srec.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

@Test
public class ScriptPlayerTest {
    public void test() throws IOException {
        try {
            ScriptPlayer player = new ScriptPlayer()
                    .init()
                    .startAndPlay(new File("src/test/resources/test_form.rb"), "com.github.srec.ui.TestForm", new String[0]);
            assertNull(player.getError());
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }

    public void testError() throws IOException {
        try {
            ScriptPlayer player = new ScriptPlayer()
                    .init()
                    .startAndPlay(new File("src/test/resources/test_form_error.rb"), "com.github.srec.ui.TestForm", new String[0]);
            assertEquals(player.getError().getLine(), 3);
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }

    @Test(enabled = false)
    public void testMethod() throws IOException {
        try {
            ScriptPlayer player = new ScriptPlayer()
                    .init()
                    .startAndPlay(new File("src/test/resources/test_form_method_call.rb"), "com.github.srec.ui.TestForm", new String[0]);
            assertNull(player.getError());
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }

}

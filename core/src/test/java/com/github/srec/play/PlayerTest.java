package com.github.srec.play;

import com.github.srec.command.parser.ParseException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

@Test
public class PlayerTest {
    public void test() throws IOException {
        try {
            Player player = new Player()
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
            Player player = new Player()
                    .init()
                    .startAndPlay(new File("src/test/resources/test_form_error.rb"), "com.github.srec.ui.TestForm", new String[0]);
            assertEquals(player.getError().getLine(), 3);
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }

    public void testMethod() throws IOException {
        try {
            Player player = new Player()
                    .init()
                    .startAndPlay(new File("src/test/resources/test_form_method_call.rb"), "com.github.srec.ui.TestForm", new String[0]);
            assertNull(player.getError());
        } catch (ParseException e) {
            e.printErrors();
            throw e;
        }
    }

}

package com.github.srec.play;

import com.github.srec.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;
import static org.testng.Assert.assertEquals;

@Test
public class ScriptPlayerTest {
    public void test() throws IOException {
        ScriptPlayer.main(new String[] {"com.github.srec.ui.TestForm", "src/test/resources/test_form.rb"});
    }

    public void testError() throws IOException {
        runMain("com.github.srec.ui.TestForm", new String[0]);
        ScriptPlayer player = new ScriptPlayer().init();
        try {
            player.play(new File("src/test/resources/test_form_error.rb"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        assertEquals(player.getError().getLine(), 3);
        closeWindows();
    }
}

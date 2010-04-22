package com.github.srec.hadoop;

import com.github.srec.play.ScriptPlayer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

@Test
public class SRecTestNGTest {
    @Parameters("script")
    public void test(String script) throws IOException, InvocationTargetException, InterruptedException {
        new ScriptPlayer().init().play(new BufferedReader(new StringReader(script)));
    }
}

package com.github.srec;

import com.github.srec.jemmy.JemmyDSL;
import org.apache.log4j.Logger;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Test
public class JRubyTest {
    private static final Logger log = Logger.getLogger(JRubyTest.class);
    private ScriptingContainer container;

    @BeforeMethod
    public void setup() {
        JemmyDSL.init();

        container = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
        List<String> loadPaths = new ArrayList<String>();
        loadPaths.add("src/main/ruby");
        container.getProvider().setLoadPaths(loadPaths);

        container.runScriptlet("require \"srec.rb\"");
        container.runScriptlet("include SRec");
        container.runScriptlet("start 'TestForm'");
    }

    public void testFormScript() throws IOException {
        testScript("src/test/resources/test_form.rb");
    }

    public void testFormLineByLine() throws IOException {
        testLineByLine("src/test/resources/test_form.rb");
    }

    public void testFormVarScript() throws IOException {
        testScript("src/test/ruby/var_def.rb");
    }

    public void testFormBlocksScript() throws IOException {
        testScript("src/test/ruby/blocks.rb");
    }
    private void testScript(String fileName) throws FileNotFoundException {
        container.runScriptlet(new FileReader(fileName), fileName);
    }

    private void testLineByLine(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            log.debug("Running line: " + line);
            container.runScriptlet(line);
        }
    }
}

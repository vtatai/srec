package com.github.srec.ui;

import com.github.srec.jemmy.JemmyDSL;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.github.srec.jemmy.JemmyDSL.*;
import static org.testng.Assert.assertTrue;

@Test
public class RecordingTest {
    public void record() {
        SRecForm.main(new String[] { });
        init();
        frame("srec");
        launch();
        frame("srec").activate();
        button("text=Record").click();
        frame("TestForm").activate();
        textField("initialValueTF").type("100");
        button("text=Ok").click();
        comboBox("calculationCB").select("Future Value");
        frame("srec").activate();
        button("text=Stop");

        String code = JemmyDSL.textArea("codeArea").text();
        assertTrue(code.contains("window_activate \"TestForm\""));
        assertTrue(code.contains("type \"initialValueTF\", \"100\""));
        assertTrue(code.contains("click \"text=Ok\""));
        assertTrue(code.contains("select \"calculationCB\""));

        frame("TestForm").close();
    }

    private void launch() {
        button("text=Launch").click();
        dialog("Launch");
        textField("mainClassTF").type("com.github.srec.ui.TestForm").type('\t');
        button("text=Launch").click();
    }

    public void load() throws IOException {
        File scriptFile = new File("src/test/ruby/test_form.rb");
        SRecForm.main(new String[] { });
        init();
        frame("srec");
        button("text=Open").click();
        dialog("Open");
        textField("label=File Name:").type(scriptFile.getCanonicalPath());
        button("text=Open").click();
        frame("srec").activate();

        String code = JemmyDSL.textArea("codeArea").text();
        assertTrue(code.contains("window_activate \"TestForm\""));
        assertTrue(code.contains("select \"calculationCB\", \"Future Value\""));
        assertTrue(code.contains("close \"TestForm\""));
        
        frame("srec").close();
    }
}

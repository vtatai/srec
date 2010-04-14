package com.github.srec.ui;

import org.testng.annotations.Test;

import java.awt.event.KeyEvent;

import static com.github.srec.play.jemmy.JemmyDSL.*;

@Test
public class RecordingTest {
    public void record() {
        SRecForm.main(new String[] { });
        frame("srec");
        button("text=Launch").click();
        dialog("Launch");
        textField("mainClassTF").type("com.github.srec.ui.TestForm").type(KeyEvent.VK_TAB);
        button("text=Launch").click();
        frame("srec");
        button("text=Record").click();
        frame("TestForm").activate();
        textField("initialValueTF").type("100");
        button("text=Ok").click();
        comboBox("calculationCB").select("Future Value");
        frame("srec");
        button("text=Stop");

        table("eventsTbl").row(0).assertColumn(0, "window_activate").assertColumn(1, "TestForm");
        table("eventsTbl").row(1)
                .assertColumn(0, "type")
                .assertColumn(1, "initialValueTF")
                .assertColumn(2, "100");
        table("eventsTbl").row(2).assertColumn(0, "click").assertColumn(1, "text=Ok");
        table("eventsTbl").row(3).assertColumn(0, "select").assertColumn(1, "calculationCB");

        frame("TestForm").close();
    }
}

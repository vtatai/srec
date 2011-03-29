package com.github.srec.play;

import com.github.srec.testng.AbstractSRecTestNGTest;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

@Test
public class PlayerTest extends AbstractSRecTestNGTest {
    private static final String TEST_SCRIPT_DIR = "src/test/resources/";

    public PlayerTest() {
        super(TEST_SCRIPT_DIR, "com.github.srec.ui.TestForm", new String[0], false);
    }

    public void test() throws IOException {
        runTest("test_form.xml");
    }

    public void testError() throws IOException {
        Player p = runTest("test_form_error.xml", false);
        p.printErrors();
        assertEquals(p.getErrors().size(), 2);
        assertEquals(p.getErrors().get(0).getLineNumber(), 19);
        assertEquals(p.getErrors().get(1).getLineNumber(), 27);
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

    public void testCall() throws IOException {
        runTest("call.xml");
    }

    public void testComboListAboveComponent() throws IOException {
        runTest("test_combo_list_above_component.xml");
    }

    public void testFindByTypeIndex() throws IOException {
        runTest("test_find_by_type_index.xml");
    }


    public void testClickCountOnTextField() throws IOException {
        runTest("test_click_count_on_text_field.xml");
    }
}

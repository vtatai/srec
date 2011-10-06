package com.github.srec.play;

import java.util.List;

import com.github.srec.testng.AbstractSRecTestNGTest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class PlayerTest extends AbstractSRecTestNGTest {
    private static final String TEST_SCRIPT_DIR = "src/test/resources/";

    public PlayerTest() {
        super(TEST_SCRIPT_DIR, "com.github.srec.ui.TestForm", new String[0], false);
    }

    public void test() {
        runTest("test_form.xml");
    }

    public void testError() {
        Player p = runTest("test_form_error.xml", false, false);
        p.printErrors();
        assertEquals(p.getErrors().size(), 2);
        assertEquals(p.getErrors().get(0).getLineNumber(), 19);
        assertEquals(p.getErrors().get(1).getLineNumber(), 27);
    }
    
    public void testErrorFailFast() {
        Player p = runTest("test_form_error.xml", false, true);
        p.printErrors();
        assertEquals(p.getErrors().size(), 1);
        assertEquals(p.getErrors().get(0).getLineNumber(), 19);
    }

    public void testFindDialog() {
        Player p = runTest("test_find_dialog.xml", false, false);
        assertEquals(p.getErrors().size(), 1);
        assertEquals(p.getErrors().get(0).getLineNumber(), 24);
    }

    public void testFindFrame() {
        Player p = runTest("test_find_frame.xml", false, false);
        assertEquals(p.getErrors().size(), 1);
        assertEquals(p.getErrors().get(0).getLineNumber(), 24);
    }

    public void testMethod() {
        runTest("test_form_method_call.xml");
    }

    public void testMethodDeep() {
        runTest("test_form_method_deep.xml");
    }

    public void testIf() {
        runTest("if.xml");
    }

    public void testWhile() {
        runTest("while.xml");
    }

    public void testCall() {
        runTest("call.xml");
    }

    public void testComboListAboveComponent() {
        runTest("test_combo_list_above_component.xml");
    }

    public void testFindByTypeIndex() {
        runTest("test_find_by_type_index.xml");
    }


    public void testClickCountOnTextField() {
        runTest("test_click_count_on_text_field.xml");
    }
    
    public void testSpecialChars() {
        runTest("test_specialchars.xml");
    }
    
    public void testNotEmptyAndEmptyTableCell() {
    	Player player = runTest("test_not_empty_table_cell.xml", false, false);
    	
    	List<PlayerError> errors = player.getErrors();
    	assertEquals(errors.size(), 1);
    		
    	PlayerError error = errors.get(0);
    	assertEquals(error.getLineNumber(), 38);
    	assertEquals(error.getTestSuite(), "test_not_empty_table");
    	assertEquals(error.getTestCase(), "1");
    	assertEquals(error.getOriginatingException().getCause().getMessage(), 
    			"Wait \"JTable with text \"Some text, but there's nothing\" " +
    			"in (4, 3) cell\" state to be reached " +
    			"(ComponentOperator.WaitStateTimeout)");
    }
	
	public void testClickHeader() {
        runTest("test_click_header.xml");
    }
    
    public void testAssertDialog() {
    	Player p = runTest("test_assert_dialog.xml", false);
        assertEquals(p.getErrors().size(), 1);
        assertEquals(p.getErrors().get(0).getLineNumber(), 17);
    }
}

package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.jemmy.JemmyDSL;
import org.netbeans.jemmy.JemmyException;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertComboBoxCommand extends JemmyEventCommand {
    public AssertComboBoxCommand() {
        super("assert_combobox", "componentLocator", "text");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        JemmyDSL.comboBox(get("componentLocator", params)).assertSelected(get("text", params));
    }
}
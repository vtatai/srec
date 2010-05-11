package com.github.srec.command.jemmy;

import com.github.srec.SRecException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.jemmy.JemmyDSL;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.operators.JComponentOperator;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
public class AssertEnabledCommand extends JemmyEventCommand {
    public AssertEnabledCommand() {
        super("assert_enabled", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        JComponentOperator op = JemmyDSL.find(params[0], JComponentOperator.class);
        try {
            op.waitComponentEnabled();
        } catch (InterruptedException e) {
            throw new SRecException(e);
        }
    }
}
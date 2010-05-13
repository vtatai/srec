package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import org.netbeans.jemmy.JemmyException;

import java.awt.event.KeyEvent;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class TypeSpecialCommand extends JemmyEventCommand {
    public TypeSpecialCommand() {
        super("type_special", "componentLocator", "text");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        int key;
        if (params[1].equals("Tab")) key = KeyEvent.VK_TAB;
        else if (params[1].equals("End")) key = KeyEvent.VK_END;
        else throw new UnsupportedFeatureException("Type special for " + params[1] + " not supported");
        textField(params[0]).type(key);
    }
}

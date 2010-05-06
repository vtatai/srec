package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
public class TypeSpecialCommand extends JemmyEventCommand {
    public TypeSpecialCommand() {
        super("type_special", "componentLocator", "text");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        char key;
        if (params[1].equals("Tab")) key = '\t';
        else throw new UnsupportedFeatureException("Type special for " + params[1] + " not supported");
        textField(params[0]).type(key);
    }
}

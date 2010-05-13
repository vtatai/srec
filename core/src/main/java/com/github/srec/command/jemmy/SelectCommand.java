package com.github.srec.command.jemmy;

import com.github.srec.Utils;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.IllegalParametersException;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.comboBox;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class SelectCommand extends JemmyEventCommand {
    public SelectCommand() {
        super("select", "componentLocator", "item");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        Map<String, String> selectedItem = Utils.parseLocator(params[1]);
        if (selectedItem.containsKey("name")) comboBox(params[0]).select(selectedItem.get("name"));
        else if (selectedItem.containsKey("index")) comboBox(params[0]).select(Integer.parseInt(selectedItem.get("index")));
        else throw new IllegalParametersException("Illegal parameters " + Utils.asString(parameters) + " for select command");
    }
}
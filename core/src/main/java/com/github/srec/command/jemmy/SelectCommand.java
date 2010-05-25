package com.github.srec.command.jemmy;

import com.github.srec.Utils;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.IllegalParametersException;
import com.github.srec.command.value.Value;
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
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        Map<String, String> selectedItem = Utils.parseLocator(coerceToString(params[1]));
        if (selectedItem.containsKey("name")) comboBox(coerceToString(get("componentLocator", params))).select(selectedItem.get("name"));
        else if (selectedItem.containsKey("index")) comboBox(coerceToString(get("componentLocator", params))).select(Integer.parseInt(selectedItem.get("index")));
        else throw new IllegalParametersException("Illegal parameters " + Utils.asString(params) + " for select command");
    }
}
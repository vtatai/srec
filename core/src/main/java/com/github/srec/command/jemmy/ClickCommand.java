package com.github.srec.command.jemmy;

import static com.github.srec.jemmy.JemmyDSL.click;
import static com.github.srec.jemmy.JemmyDSL.textField;
import static com.github.srec.jemmy.JemmyDSL.tree;

import java.awt.event.InputEvent;
import java.util.Map;

import org.netbeans.jemmy.JemmyException;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.command.value.StringValue;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class ClickCommand extends JemmyEventCommand {
    public static final String COUNT = "count";
    public static final String TEXT_COLUMN = "textColumn";
    public static final String MODIFIERS = "modifiers";
    public static final String REQUEST_FOCUS = "requestFocus";
    public static final String NODE = "node";
    public static final String INDEX = "index";
    public static final String BUTTON = "button";

    public static enum Button {
        left(InputEvent.BUTTON1_MASK), 
        right(InputEvent.BUTTON2_MASK);
        
        private int mask;
        
        private Button(int mask) {
            this.mask = mask;
        }
        
        public int getMask() {
            return mask;
        }
    }
    
    public ClickCommand() {
        super("click", param(LOCATOR), param(MODIFIERS, Type.STRING, true, null),
                param(TEXT_COLUMN, Type.NUMBER, true, null),
                param(COUNT, Type.NUMBER, true, new NumberValue("1")),
                param(NODE, Type.STRING, true, null),
                param(INDEX, Type.NUMBER, true, new NumberValue("0")),
                param(BUTTON, Type.STRING, true, new StringValue(Button.left.name())),
                param(REQUEST_FOCUS, Type.BOOLEAN, true, new BooleanValue(false)));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        String button = asString(BUTTON, params, ctx);
        Button b = Button.valueOf(button);
        if (params.get(NODE) != null) {
            tree(coerceToString(params.get(LOCATOR), ctx))
                    .click(asBigDecimal(COUNT, params).intValue(),
                            asString(NODE, params, ctx), b);
        } else if (params.get(TEXT_COLUMN) == null)  {
            click(coerceToString(params.get(LOCATOR), ctx), asBigDecimal(COUNT, params).intValue(),
                    asString(MODIFIERS, params, ctx), asBoolean(REQUEST_FOCUS, params), asBigDecimal(INDEX, params).intValue(), b);
        } else {
            textField(coerceToString(params.get(LOCATOR), ctx)).clickCharPosition(asBigDecimal(TEXT_COLUMN, params).intValue(),
                    asString(MODIFIERS, params, ctx), asBigDecimal(COUNT, params).intValue(), b);
        }
    }
}
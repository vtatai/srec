package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.AssertionFailedException;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.jemmy.JemmyDSL.Window;

import org.netbeans.jemmy.JemmyException;

import java.awt.Component;
import javax.swing.JList;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.ListModel;

@SRecCommand
public class AssertListContentCommand extends JemmyEventCommand {
    public AssertListContentCommand() {
        super("assert_list_content", params(LOCATOR, Type.STRING, "content", Type.STRING));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        String locator = coerceToString(params.get(LOCATOR), ctx);

        Window window = JemmyDSL.currentWindow();
        Component object = JemmyDSL.findComponent(locator, window.getComponent().getSource());

        ListModel lstModel = null;

        if (object instanceof JList) {
            JList jl = (JList) object;
            lstModel = jl.getModel();
        } else if (object instanceof JComboBox) {
            JComboBox jbc = (JComboBox) object;
            lstModel = jbc.getModel();
        } else {
            throw new AssertionFailedException("Component [" + locator + "] is a " 
                                               + object.getClass().getName() + ", and not a JList or JComboBox");
        }

        String[] expected = coerceToArray(params.get("content"), ctx);
        int size = lstModel.getSize();
        if (expected.length != size)
            throw new AssertionFailedException("Different size, expected " + expected.length + " but was " + size);
        for (int i = 0; i < size; i++) {
            Object o = lstModel.getElementAt(i);
            String actual = (o instanceof String) ? (String) o : o.toString();
            if (!expected[i].equals(actual))
                throw new AssertionFailedException("Difference found at " + i + "th element , expected "
                                                   + expected[i] + " but was " + lstModel.getElementAt(i));
        }
    }
}
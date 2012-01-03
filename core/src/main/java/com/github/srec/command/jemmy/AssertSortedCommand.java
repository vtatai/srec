package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.AssertionFailedException;
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
public class AssertSortedCommand extends JemmyEventCommand {
    public AssertSortedCommand() {
        super("assert_sorted", params(LOCATOR));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
    	String locator = coerceToString(params.get(LOCATOR), ctx);
    	
    	Window window = JemmyDSL.currentWindow();
    	Component object = JemmyDSL.findComponent(locator, 
                window.getComponent().getSource());
    	
    	ListModel lstModel = null;

        if (object instanceof JList) {
            JList jl = (JList) object;
            lstModel = jl.getModel();
        } else if (object instanceof JComboBox) {
            JComboBox jbc = (JComboBox) object;
            lstModel = jbc.getModel();
        } else {
        	throw new AssertionFailedException("Component [" + locator + "] is a " 
        			+ object.getClass().getName() +	", and not a JList or JComboBox");
        }

        if (lstModel.getSize() > 1) {
            Comparable<Object> tc = null;
            Comparable<Object> tcProximo = null;

            for (int i=0; i<lstModel.getSize()-1; i++) {
                tc = (Comparable<Object>) lstModel.getElementAt(i);
                tcProximo = (Comparable<Object>) lstModel.getElementAt(i + 1);
                                
                if (tcProximo.compareTo(tc) < 0) {
                    i = lstModel.getSize();
                    String message = "Order failed at element <" + tc
                            + "> before element <" + tcProximo + ">";
                    throw new AssertionFailedException(message);
                }
            }
        }
    }
}
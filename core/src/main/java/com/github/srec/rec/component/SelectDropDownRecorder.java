package com.github.srec.rec.component;

import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;
import com.github.srec.rec.common.ComponentDecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.github.srec.Utils.createParameterMap;

/**
 * Understands recording item listeners.
 *
 * @author Vivek Prahlad
 */
public class SelectDropDownRecorder extends AbstractComponentRecorder implements ItemListener {
    private ComponentDecoder decoder;
    private static JList DUMMY_LIST = new JList();
    private ComponentVisibility visibility;

    public SelectDropDownRecorder(EventRecorder recorder, ComponentDecoder decoder, ComponentVisibility visibility) {
        super(recorder, JComboBox.class);
        this.decoder = decoder;
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        combo(component).addItemListener(this);
    }

    void componentHidden(Component component) {
        combo(component).removeItemListener(this);
    }

    private JComboBox combo(Component component) {
        return (JComboBox) component;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox combo = (JComboBox) e.getSource();
            if (!visibility.isShowingAndHasFocus(combo)) return;
            ListCellRenderer renderer = combo.getRenderer();
            Component rendererComponent = renderer.getListCellRendererComponent(DUMMY_LIST, combo.getSelectedItem(), combo.getSelectedIndex(), false, false);
            recorder.record(new MethodCallEventCommand("select", combo, null,
                    createParameterMap("locator", combo.getName(), "item", decoder.decode(rendererComponent))));
        }
    }
}

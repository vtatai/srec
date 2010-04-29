package com.github.srec.rec.component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Default component visibility implementation
 *
 * @author Vivek Prahlad
 */
public class DefaultComponentVisibility implements ComponentVisibility {
    public boolean isShowing(Component component) {
        return component.isShowing();
    }

    public boolean isShowingAndHasFocus(Component component) {
        return isShowing(component) && component.hasFocus();
    }
}

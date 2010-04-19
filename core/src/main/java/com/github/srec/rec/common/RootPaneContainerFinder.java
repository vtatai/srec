package com.github.srec.rec.common;

import javax.swing.*;
import java.awt.*;

/**
 * Finds a root pane container.
 *
 * @author Vivek Prahlad
 */
public class RootPaneContainerFinder {
    public Component findRootPane(Component focusOwner) {
        if (focusOwner == null) return null;
        if (focusOwner instanceof RootPaneContainer && !(focusOwner instanceof JInternalFrame)) return focusOwner;
        if (focusOwner.getParent() != null) return findRootPane(focusOwner.getParent());
        return null;
    }
}

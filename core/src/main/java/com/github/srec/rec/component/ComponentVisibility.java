package com.github.srec.rec.component;

import java.awt.*;

/**
 * Informs clients about whether a component is visible or not.
 *
 * @author Vivek Prahlad
 */
public interface ComponentVisibility {
    public boolean isShowing(Component compnent);

    public boolean isShowingAndHasFocus(Component compnent);
}

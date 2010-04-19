package com.github.srec.rec.common;

import javax.swing.*;
import java.awt.*;

/**
 * Decodes text from JLabels.
 *
 * @author Vivek Prahlad
 */
public class JLabelDecoder implements ComponentDecoder {
    public String decode(Component renderer) {
        return ((JLabel) renderer).getText();
    }
}

package com.github.srec.rec.common;

import javax.swing.*;
import java.awt.*;

/**
 * Decodes text from JCheckBoxes.
 *
 * @author Vivek Prahlad
 */
public class JCheckBoxDecoder implements ComponentDecoder {

    public String decode(Component renderer) {
        return Boolean.toString(((JCheckBox) renderer).isSelected());
    }
}

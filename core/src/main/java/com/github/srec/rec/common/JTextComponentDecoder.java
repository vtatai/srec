package com.github.srec.rec.common;

import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Understands decoding JTextComponents
 *
 * @author Vivek Prahlad
 */
public class JTextComponentDecoder implements ComponentDecoder {
    public String decode(Component renderer) {
        return ((JTextComponent) renderer).getText();
    }
}

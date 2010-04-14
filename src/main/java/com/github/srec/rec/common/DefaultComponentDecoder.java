package com.github.srec.rec.common;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes default swing components.
 *
 * @author Vivek Prahlad
 */
public class DefaultComponentDecoder implements ComponentDecoder {
    private Map componentDecoderMap;
    private ComponentDecoder textComponentDecoder = new JTextComponentDecoder();
    private ComponentDecoder jlabelDecoder = new JLabelDecoder();

    public DefaultComponentDecoder() {
        componentDecoderMap = new HashMap();
        componentDecoderMap.put(JCheckBox.class, new JCheckBoxDecoder());
    }

    public String decode(Component renderer) {
        if (renderer instanceof JTextComponent) return textComponentDecoder.decode(renderer);
        if (renderer instanceof JLabel) return jlabelDecoder.decode(renderer);
        if (componentDecoderMap.containsKey(renderer.getClass())) {
            ComponentDecoder decoder = (ComponentDecoder) componentDecoderMap.get(renderer.getClass());
            return decoder.decode(renderer);
        } else {
            return "Could not decode component of type: " + renderer.getClass().getName();
        }
    }

    public void registerDecoder(Class componentClass, ComponentDecoder decoder) {
        componentDecoderMap.put(componentClass, decoder);
    }
}

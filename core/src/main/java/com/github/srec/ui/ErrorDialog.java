package com.github.srec.ui;

import javax.swing.*;

/**
 * @author Victor Tatai
 */
public class ErrorDialog {
    public static void show(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

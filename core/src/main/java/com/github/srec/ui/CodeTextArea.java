package com.github.srec.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * Adds some useful methods to the code text area.
 * 
 * @author Victor Tatai
 */
public class CodeTextArea extends RSyntaxTextArea {
    /**
     * Replaces the content of the current caret line.
     *
     * @param newLine The new line
     */
    public void replaceCurrentLine(String newLine) {
        int start = getLineStartOffsetOfCurrentLine();
        int end = getLineEndOffsetOfCurrentLine();
        replaceRange(newLine, start, end);
    }
}

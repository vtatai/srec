package com.github.srec.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.text.BadLocationException;

/**
 * Adds some useful methods to the code text area.
 * 
 * @author Victor Tatai
 */
public class CodeTextArea extends RSyntaxTextArea {
    /**
     * Gets the current line.
     *
     * @return The line
     * @throws BadLocationException In case there is an error
     */
    public String getCurrentLine() throws BadLocationException {
        int start = getLineStartOffsetOfCurrentLine();
        int end = getLineEndOffsetOfCurrentLine();
        select(start, end);
        return getText(start, end - start);
    }

    /**
     * Returns true if the caret is at the last line.
     *
     * @return true if at the last line
     */
    public boolean isLastLine() {
        int end = getLineEndOffsetOfCurrentLine();
        int length = getText().length();
        return (end + 1 >= length);
    }

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

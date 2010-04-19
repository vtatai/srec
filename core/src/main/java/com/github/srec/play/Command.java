package com.github.srec.play;

/**
 * A command is used to control the UI under test. Each command should map exactly to one verb in the DSL. 
 *
 * @author Victor Tatai
 */
public interface Command {
    String getName();
    void run(String... params);
}

package com.github.srec.play;

public interface Command {
    String getName();
    void run(String... params);
}

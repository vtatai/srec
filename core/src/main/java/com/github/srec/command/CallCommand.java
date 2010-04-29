package com.github.srec.command;

import com.github.srec.UnsupportedFeatureException;

import java.util.ArrayList;
import java.util.List;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class CallCommand implements Command {
    private String name;
    private List<String> parameters = new ArrayList<String>();

    public CallCommand(String name, List<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public void run() {
        throw new UnsupportedFeatureException("Call not yet supported");
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }
}

package com.github.srec.command;

import java.util.ArrayList;
import java.util.List;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class CallCommand extends BaseCommand {
    protected List<String> parameters = new ArrayList<String>();

    public CallCommand(String name) {
        super(name);
    }

    public void addParameter(String param) {
        parameters.add(param);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return getName();
    }
}

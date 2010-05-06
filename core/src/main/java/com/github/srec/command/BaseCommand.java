package com.github.srec.command;

import org.antlr.runtime.tree.CommonTree;

/**
 * Base class for commands.
 * @author Victor Tatai
 */
public abstract class BaseCommand implements Command {
    protected String name;
    protected CommonTree tree;

    protected BaseCommand(String name) {
        this.name = name;
    }

    protected BaseCommand(String name, CommonTree tree) {
        this(name);
        this.tree = tree;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CommonTree getTree() {
        return tree;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTree(CommonTree tree) {
        this.tree = tree;
    }
}

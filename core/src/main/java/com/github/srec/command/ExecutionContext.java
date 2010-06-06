/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.command;

import com.github.srec.command.base.Command;
import com.github.srec.command.base.CommandSymbol;
import com.github.srec.play.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents a script execution context (EC), containing commands to be executed and a very simple symbol table which
 * contains method definitions.
 * 
 * @author Victor Tatai
 */
public class ExecutionContext {
    private TestSuite testSuite;
    private TestCase testCase;
    private List<Command> commands = new ArrayList<Command>();
    private Map<String, CommandSymbol> symbols = new HashMap<String, CommandSymbol>();
    /**
     * The player executing this EC.
     */
    private Player player;
    /**
     * File from where this EC was read from. May be null.
     */
    private File file;
    /**
     * Load paths from where required scripts should be searched.
     */
    private List<String> loadPath = new ArrayList<String>();

    public ExecutionContext(TestSuite ts, TestCase tc, File file, String... loadPaths) {
        testSuite = ts;
        testCase = tc;
        this.file = file;
        loadPath.addAll(Arrays.asList(loadPaths));
    }

    public ExecutionContext(TestSuite ts, TestCase tc, Player player, File file, String... loadPaths) {
        this(ts, tc, file, loadPaths);
        this.player = player;
    }

    /**
     * Copy constructor, does a shallow copy of the commands, symbols and load path.
     *
     * @param other The other execution context
     */
    public ExecutionContext(ExecutionContext other) {
        testSuite = other.testSuite;
        testCase = other.testCase;
        commands.addAll(other.getCommands());
        symbols.putAll(other.getSymbols());
        loadPath.addAll(other.getLoadPath());
    }
    
    /**
     * Method that should be used to effectively locate symbols.
     *
     * @param name The symbol name
     * @return The symbol, null if not found
     */
    public CommandSymbol findSymbol(String name) {
        return symbols.get(name);
    }
    
    public void addCommand(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public void addSymbol(CommandSymbol cmd) {
        symbols.put(cmd.getName(), cmd);
    }

    public void addAllSymbols(ExecutionContext context) {
        symbols.putAll(context.symbols);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getParentPath() throws IOException {
        return file.getParentFile().getCanonicalPath();
    }

    public String getCanonicalPath() throws IOException {
        return file.getCanonicalPath();
    }

    public File getFile() {
        return file;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<String> getLoadPath() {
        return loadPath;
    }

    public Map<String, CommandSymbol> getSymbols() {
        return symbols;
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }
}

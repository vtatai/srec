package com.github.srec.play;

import com.github.srec.Utils;
import com.github.srec.command.Command;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.jemmy.JemmyExecutionContextFactory;
import com.github.srec.command.parser.ScriptParser;
import com.github.srec.jemmy.JemmyDSL;
import org.antlr.runtime.RecognitionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;

/**
 * Class which plays srec script files.
 *  
 * @author Victor Tatai
 */
public class ScriptPlayer {
    private ScriptPlayerError error;
    private boolean throwError;
    private long commandInterval = 50;

    public ScriptPlayer init() {
        JemmyDSL.init();
        return this;
    }

    public ScriptPlayer startAndPlay(File file, String className, String[] args) throws IOException, RecognitionException {
        Utils.runMain(className, args);
        play(file);
        closeWindows();
        return this;
    }

    public ScriptPlayer play(File file) throws IOException, RecognitionException {
        return play(new FileInputStream(file));
    }

    public ScriptPlayer play(InputStream is) throws IOException, RecognitionException {
        ExecutionContext context = new JemmyExecutionContextFactory().create();
        ScriptParser.parse(context, is);
        for (Command command : context.getCommands()) {
            try {
                command.run(context);
            } catch (CommandExecutionException e) {
                handleError(command, e);
                break;
            }
            try {
                Thread.sleep(commandInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    private void handleError(Command command, CommandExecutionException e) {
        error = new ScriptPlayerError(command.getTree().getLine(), command.getTree().getText(), e);
        System.err.println("Error on line " + command.getTree().getLine() + ":");
        System.err.println(command.getTree().toStringTree());
        e.printStackTrace(System.err);
    }

    public ScriptPlayerError getError() {
        return error;
    }

    public boolean isThrowError() {
        return throwError;
    }

    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }

    public long getCommandInterval() {
        return commandInterval;
    }

    public void setCommandInterval(long commandInterval) {
        this.commandInterval = commandInterval;
    }

    public static void main(final String[] args) throws IOException, RecognitionException {
        runMain(args[0], null);

        ScriptPlayer scriptPlayer = new ScriptPlayer().init();
        scriptPlayer.play(new File(args[1]));
        if (scriptPlayer.getError() != null) {
            System.err.println(scriptPlayer.getError());
        }

        closeWindows();
    }

}

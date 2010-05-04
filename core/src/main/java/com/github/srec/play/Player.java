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
import java.util.List;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;

/**
 * Class which plays srec scripts / commands.
 *  
 * @author Victor Tatai
 */
public class Player {
    private PlayerError error;
    private boolean throwError;
    private long commandInterval = 50;

    public Player init() {
        JemmyDSL.init();
        return this;
    }

    public Player startAndPlay(File file, String className, String[] args) throws IOException, RecognitionException {
        Utils.runMain(className, args);
        play(file);
        closeWindows();
        return this;
    }

    public Player play(File file) throws IOException, RecognitionException {
        return play(new FileInputStream(file), file.getParentFile().getCanonicalPath());
    }

    public Player play(InputStream is, String currentPath) throws IOException, RecognitionException {
        ExecutionContext context = new JemmyExecutionContextFactory().create(currentPath);
        ScriptParser.parse(context, is);
        context.setPlayer(this);
        play(context, context.getCommands());
        return this;
    }

    public void play(ExecutionContext context, List<Command> commands) {
        for (Command command : commands) {
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
    }

    private void handleError(Command command, CommandExecutionException e) {
        error = new PlayerError(command.getTree().getLine(), command.getTree().getText(), e);
        System.err.println("Error on line " + command.getTree().getLine() + ":");
        System.err.println(command.getTree().toStringTree());
        e.printStackTrace(System.err);
    }

    public PlayerError getError() {
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

        Player player = new Player().init();
        player.play(new File(args[1]));
        if (player.getError() != null) {
            System.err.println(player.getError());
        }

        closeWindows();
    }

}

package com.github.srec.play;

import com.github.srec.Utils;
import com.github.srec.command.CommandSerializer;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.play.exception.PlayerException;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Class which plays srec script files.
 *  
 * @author Victor Tatai
 */
public class ScriptPlayer {
    private ScriptPlayerError error;
    private boolean throwError;
    private long commandInterval = 0;

    public ScriptPlayer init() {
        JemmyDSL.init();
        return this;
    }

    public ScriptPlayer play(File file) throws IOException {
        play(new FileInputStream(file));
        return this;
    }

    public ScriptPlayer startAndPlay(File file, String className, String[] args) throws IOException {
        Utils.runMain(className, args);
        play(file);
        closeWindows();
        return this;
    }

    public void play(InputStream is) throws IOException {
        play(new BufferedReader(new InputStreamReader(is)));
    }

    /**
     * Executes line by line the script. Does not load the entire script at once in order to speed up test start,
     *
     * @param reader The reader
     * @throws IOException In case there is a problem reading from reader
     */
    public void play(BufferedReader reader) throws IOException {
        int lineCounter = 1;
        error = null;
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                CommandSerializer.parse(line).run();
            } catch (PlayerException e) {
                handleError(lineCounter, e);
                if (throwError) throw e;
                return;
            }
            try {
                if (commandInterval > 0) Thread.sleep(commandInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lineCounter++;
        }
    }

    private void handleError(int line, PlayerException e) {
        error = new ScriptPlayerError(line, e);
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

    public static void main(final String[] args) throws IOException {
        runMain(args[0], null);

        ScriptPlayer scriptPlayer = new ScriptPlayer().init();
        scriptPlayer.play(new File(args[1]));
        if (scriptPlayer.getError() != null) {
            System.err.println(scriptPlayer.getError());
        }

        closeWindows();
    }

    public static class ScriptPlayerError {
        private int line;
        private PlayerException originatingException;

        public ScriptPlayerError(int line, PlayerException originatingException) {
            this.line = line;
            this.originatingException = originatingException;
        }

        public int getLine() {
            return line;
        }

        public PlayerException getOriginatingException() {
            return originatingException;
        }

        @Override
        public String toString() {
            return "Script error on line " + line + ", message:\n" + originatingException.getMessage();
        }
    }
}

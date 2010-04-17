package com.github.srec.play;

import com.github.srec.Utils;
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

public class ScriptPlayer {
    private Player player = new Player();
    private ScriptPlayerError error;
    private boolean throwError;

    public ScriptPlayer init() {
        player.init();
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

    public void play(BufferedReader reader) throws IOException {
        int lineCounter = 1;
        error = null;
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                play(line);
            } catch (PlayerException e) {
                handleError(lineCounter, e);
                if (throwError) throw e;
                return;
            }
            lineCounter++;
        }
    }

    private void handleError(int line, PlayerException e) {
        error = new ScriptPlayerError(line, e);
    }

    public void play(String line) {
        assert line != null;
        if (line.indexOf("#") != -1) {
            line = line.substring(0, line.indexOf("#"));
        }
        if (StringUtils.isBlank(line)) return;
        int i = line.indexOf(' ');
        if (i == -1) {
            player.play(line);
            return;
        }
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher m = pattern.matcher(line);
        List<String> params = new ArrayList<String>();
        int lastmatch = 0;
        while (m.find(lastmatch)) {
            params.add(m.group(1));
            lastmatch = m.end();
        }
        player.play(line.substring(0, i), params.toArray(new String[params.size()]));
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

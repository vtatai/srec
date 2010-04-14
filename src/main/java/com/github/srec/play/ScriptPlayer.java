package com.github.srec.play;

import org.apache.commons.lang.StringUtils;

import java.io.*;

import static com.github.srec.Utils.runMain;

public class ScriptPlayer {
    private Player player = new Player();

    public ScriptPlayer init() {
        player.init();
        return this;
    }

    public void play(File file) throws IOException {
        play(new FileInputStream(file));
    }

    public void play(InputStream is) throws IOException {
        play(new BufferedReader(new InputStreamReader(is)));
    }

    public void play(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            play(line);
        }
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
        String[] params = line.substring(i + 1).split(",");
        for (int j = 0; j < params.length; j++) {
            String param = params[j].trim();
            if (param.startsWith("\"") && param.endsWith("\"")) param = param.substring(1, param.length() - 1);
            params[j] = param;
        }
        player.play(line.substring(0, i), params);
    }

    public static void main(String[] args) throws IOException {
        runMain(args[0], null);
        new ScriptPlayer().init().play(new File(args[1]));
    }
}

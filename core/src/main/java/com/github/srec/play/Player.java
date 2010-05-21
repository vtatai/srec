package com.github.srec.play;

import com.github.srec.Utils;
import com.github.srec.debug.JRubyDebugger;
import com.github.srec.jemmy.JemmyDSL;
import org.apache.log4j.Logger;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import static com.github.srec.Utils.closeWindows;
import static com.github.srec.Utils.runMain;

/**
 * Class which plays srec scripts / commands.
 *  
 * @author Victor Tatai
 */
public class Player {
    private static final Logger log =  Logger.getLogger(Player.class);
    private boolean throwError;
    private long commandInterval = 50;
    private ScriptingContainer container;

    public Player init() {
        JemmyDSL.init();
        return this;
    }

    public Player startAndPlay(File file, String className, String[] args) throws IOException {
        Utils.runMain(className, args);
        play(file);
        closeWindows();
        return this;
    }

    public Player play(File file) throws IOException {
        if (file == null) return null;
        if (file.isDirectory()) {
            log.info("Playing files inside dir: " + file.getCanonicalPath());
            // Run all scripts inside a directory
            File[] files = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".rb");
                }
            });
            for (File file1 : files) {
                play(file1);
            }
            File[] subdirs = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            for (File subdir : subdirs) {
                play(subdir);
            }
            return this;
        } else {
            log.info("Playing file: " + file.getCanonicalPath());
            try {
                return playFile(file);
            } catch (InterruptedException e) {
                throw new PlayerException(e);
            }
        }
    }

    /**
     * Plays an input stream.
     *
     * @param file The file being played
     * @return The Player
     * @throws IOException In case there is an error reading from the input stream
     */
    public Player playFile(File file) throws IOException, InterruptedException {
        JRubyDebugger d = new JRubyDebugger();
        d.start(file.getCanonicalPath());
        while (d.isRunning()) {
            d.step();
            Thread.sleep(1000);
        }
        return this;
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

    public static void main(final String[] args) throws IOException {
        runMain(args[0], null);

        Player player = new Player().init();
        player.play(new File(args[1]));

        closeWindows();
    }

}

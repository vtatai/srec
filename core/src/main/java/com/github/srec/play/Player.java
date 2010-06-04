package com.github.srec.play;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextFactory;
import com.github.srec.command.TestCase;
import com.github.srec.command.TestSuite;
import com.github.srec.command.base.Command;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.parser.ParseException;
import com.github.srec.command.parser.Parser;
import com.github.srec.command.parser.ParserFactory;
import com.github.srec.jemmy.JemmyDSL;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.srec.util.Utils.closeWindows;
import static com.github.srec.util.Utils.runSwingMain;

/**
 * Class which plays srec scripts / commands.
 *  
 * @author Victor Tatai
 */
public class Player {
    private static final Logger log =  Logger.getLogger(Player.class);
    private List<PlayerError> errors = new ArrayList<PlayerError>();
    private long commandInterval = 50;
    private Parser parser;

    /**
     * Class to run before each test case. May be null.
     */
    private String classToRun;
    /**
     * Arguments when running the class to run. May be null.
     */
    private String[] classToRunArgs;

    public Player init() {
        JemmyDSL.init();
        parser = ParserFactory.create();
        return this;
    }

    public Player play(File file, String testCase, String className, String[] args) throws IOException {
        classToRun = className;
        classToRunArgs = args;
        play(file, testCase);
        return this;
    }

    public Player play(File file) throws IOException {
        return play(file, null);
    }

    public Player play(File file, String testCase) throws IOException {
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
            return play(new FileInputStream(file), file, testCase);
        }
    }

    /**
     * Plays a script.
     *
     * @param is The input stream from where to read the script
     * @param file The originating file, may be null
     * @param testCaseName The name of the test case to play, if null all test cases are played
     * @return The player
     * @throws IOException in case there is an error reading the file
     */
    public Player play(InputStream is, File file, String testCaseName) throws IOException {
        ExecutionContext context = ExecutionContextFactory.getInstance().create(null, null, file, file.getParentFile().getCanonicalPath());
        TestSuite suite = parser.parse(context, is, file.getCanonicalPath());
        if (parser.getErrors().size() > 0) throw new ParseException(parser.getErrors());
        log.debug("Launching test suite: " + suite.getName());
        for (TestCase testCase : suite.getTestCases()) {
            if (testCaseName != null && !testCase.getName().equals(testCaseName)) continue;
            if (classToRun != null) runSwingMain(classToRun, classToRunArgs);
            log.debug("Launching test case: " + testCase.getName());
            ExecutionContext testCaseEC = testCase.getExecutionContext();
            testCaseEC.setPlayer(this);
            testCaseEC.setTestCase(testCase);
            play(testCaseEC);
            if (classToRun != null) closeWindows();
        }
        return this;
    }

    public Command.CommandFlow play(ExecutionContext context) {
        for (Command command : context.getCommands()) {
            log.debug("Running line: " + getLine(command) + ", command: " + command);
            try {
                Command.CommandFlow flow = command.run(context);
                if (flow == Command.CommandFlow.NEXT) {} 
                else if (flow == Command.CommandFlow.EXIT) return Command.CommandFlow.EXIT;
                else throw new PlayerException("Flow management instruction " + flow + "  from command "
                            + command.getName() + " not supported");
            } catch (CommandExecutionException e) {
                handleError(context.getTestSuite(), context.getTestCase(), command, e);
                break;
            }
            try {
                Thread.sleep(commandInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Command.CommandFlow.NEXT;
    }

    private String getLine(Command command) {
        if (command.getLocation() == null) return "<NO LINE>";
        return "" + command.getLocation().getLineNumber();
    }

    private void handleError(TestSuite testSuite, TestCase testCase, Command command, CommandExecutionException e) {
        PlayerError error = new PlayerError(testSuite == null ? "" : testSuite.getName(),
                testCase == null ? "" : testCase.getName(), command.getLocation(), e);
        System.err.println("Error on line " + command.getLocation().getLineNumber() + ":");
        System.err.println(command.getLocation().getLine());
        errors.add(error);
    }

    public List<PlayerError> getErrors() {
        return errors;
    }

    public long getCommandInterval() {
        return commandInterval;
    }

    public void setCommandInterval(long commandInterval) {
        this.commandInterval = commandInterval;
    }

    /**
     * Prints all errors in stderr.
     */
    public void printErrors() {
        for (PlayerError playerError : getErrors()) {
            System.err.println(playerError);
        }
    }

    public static void main(final String[] args) throws IOException {
        runSwingMain(args[0], null);

        Player player = new Player().init();
        player.play(new File(args[1]));
        player.printErrors();
        closeWindows();
    }

}

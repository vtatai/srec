package com.github.srec.command;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.Utils;
import com.github.srec.command.jemmy.JemmyCommandFactory;
import com.github.srec.play.exception.PlayerException;
import com.github.srec.rec.EventReaderException;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.srec.Utils.quote;
import static com.github.srec.Utils.unquote;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Class used to write and read events from a file. It follows the Ruby DSL script format.
 * 
 * @author Victor Tatai
 */
public class CommandSerializer {
    private static CommandFactory commandFactory = new JemmyCommandFactory();

    public static void write(File file, List<Command> commands) throws IOException {
        Writer writer = new Writer(file);
        try {
            for (Command event : commands) {
                print(event, writer);
            }
        } finally {
            try { writer.flush(); }
            finally { writer.close(); }
        }
    }

    private static void print(Command command, Writer writer) {
        if (StringUtils.isBlank(command.getName())) return;
        if (command instanceof EventCommand) print((EventCommand) command, writer);
        else if (command instanceof CallCommand) print((CallCommand) command, writer);
        else if (command instanceof DefCommand) print((DefCommand) command, writer);
        else throw new EventReaderException("Command cannot be serialized: " + command);
    }

    private static void print(EventCommand event, Writer writer) {
        if (StringUtils.isBlank(event.getComponentLocator())) return;
        StringBuilder strb = new StringBuilder(event.getName()).append(" ").append(quote(event.getComponentLocator()));
        for (String arg : event.getParams()) {
            strb.append(", ").append(quote(arg));
        }
        writer.println(strb.toString());
    }

    private static void print(CallCommand command, Writer writer) {
        StringBuilder strb = new StringBuilder(command.getName());
        for (String arg : command.getParameters()) {
            strb.append(arg).append(", ");
        }
        String s = strb.toString();
        if (s.endsWith(", ")) s = s.substring(0, s.length() - 2);
        writer.println(s);
    }

    private static void print(DefCommand command, Writer writer) {
        StringBuilder strb = new StringBuilder("def ").append(command.getName()).append("(");
        for (String arg : command.getParameters()) {
            strb.append(arg).append(", ");
        }
        String s = strb.toString();
        if (s.endsWith(", ")) strb.delete(s.length() - 2, s.length());
        strb.append(")");
        writer.println(strb.toString());
        writer.ident();
        for (Command recorderCommand : command.getCommands()) {
            print(recorderCommand, writer);
        }
        writer.decIdent();
        writer.println("end");
    }

    public static List<Command> read(File file) throws IOException {
        List<Command> commands = new ArrayList<Command>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            AggregateCommand aggregate = null;
            String line;
            while ((line = reader.readLine()) != null) {
                Command command = parse(line);
                if (command instanceof AggregateCommand) {
                    if (aggregate != null) throw new UnsupportedFeatureException("Aggregate command inside another aggregate not supported");
                    aggregate = (AggregateCommand) command;
                    commands.add(aggregate);
                    continue;
                }
                if (aggregate != null) {
                    boolean keepAggregating = aggregate.add(command);
                    if (!keepAggregating) aggregate = null;
                } else {
                    commands.add(command);
                }
            }
        } finally {
            reader.close();
        }
        return commands;
    }

    public static Command parse(String line) {
        assert line != null;
        // Strip comments
        if (line.indexOf("#") != -1) {
            line = line.substring(0, line.indexOf("#"));
        }
        if (StringUtils.isBlank(line)) return null;
        line = line.trim();
        if (line.startsWith("def ")) {
            // Method definition
            int parIndex = line.indexOf('(');
            String name = parIndex == -1 ? line.substring(4).trim() : line.substring(4, parIndex).trim();
            if (isBlank(name)) throw new PlayerException("No name assigned to method: " + line);
            if (parIndex == -1) {
                return commandFactory.buildCommand("def", name);
            } else {
                String remaining = line.substring(parIndex + 1, line.lastIndexOf(')')).trim();
                String[] methodParams = Utils.trimArray(remaining.split(","));
                String[] params = new String[methodParams.length + 1];
                params[0] = name;
                System.arraycopy(methodParams, 0, params, 1, methodParams.length);
                return commandFactory.buildCommand("def", params);
            }
        } else {
            line = line.replace('(', ' '); // Make parsing easier
            line = line.replace(')', ' ');
            int i = line.indexOf(' ');
            String command = line.substring(0, i == -1 ? line.length() : i).trim();
            if (isBlank(command)) throw new PlayerException("No command name : " + line);
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher m = pattern.matcher(line);
            List<String> params = new ArrayList<String>();
            int lastmatch = 0;
            while (m.find(lastmatch)) {
                params.add(m.group(1));
                lastmatch = m.end();
            }
            return commandFactory.buildCommand(command, params.toArray(new String[params.size()]));
        }
    }

    private static class Writer {
        private PrintWriter writer;
        private int ident;

        public Writer(File file) throws IOException {
            writer = new PrintWriter(new FileWriter(file));
        }

        public void println(String line) {
            writer.println(genIdent() + line);
        }

        private String genIdent() {
            StringBuilder strb = new StringBuilder();
            for (int i = 0; i < ident; i++) {
                strb.append(" ");
            }
            return strb.toString();
        }

        public void ident() {
            ident++;
        }

        public void decIdent() {
            ident--;
        }

        public void flush() {
            writer.flush();
        }

        public void close() {
            writer.close();
        }
    }
}

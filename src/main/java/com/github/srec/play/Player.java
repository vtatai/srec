package com.github.srec.play;

import com.github.srec.RecorderEvent;
import com.github.srec.play.exception.PlayerException;
import com.github.srec.play.exception.TimeoutException;
import com.github.srec.play.exception.UnsupportedCommandException;
import com.github.srec.play.jemmy.*;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private Map<String, Command> commandMap = new HashMap<String, Command>();

    public void play(String command, String... params) {
        final Command cmd = commandMap.get(command);
        if (cmd == null) throw new UnsupportedCommandException(command);
        try {
            cmd.run(params);
        } catch (RuntimeException e) {
            throw convertException(e, cmd, params);
        }
    }

    private RuntimeException convertException(RuntimeException e, Command cmd, String[] params) {
        if (e instanceof TimeoutExpiredException) throw new TimeoutException(cmd, params);
        throw new IllegalArgumentException();
    }

    public void addCommand(Command cmd) {
        commandMap.put(cmd.getName(), cmd);
    }

    public void init() {
        addCommand(new ClickCommand());
        addCommand(new TypeCommand());
        addCommand(new TypeSpecialCommand());
        addCommand(new WindowActivateCommand());
        addCommand(new SelectCommand());
        addCommand(new CloseCommand());

        try {
            JemmyDSL.init();
        } catch (IOException e) {
            throw new PlayerException(e.getMessage());
        }
    }

    public void play(final List<RecorderEvent> events) {
        Thread t = new Thread() {
            @Override
            public void run() {
                for (RecorderEvent event : events) {
                    play(event);
                }
            }
        };
        t.start();
    }

    private void play(RecorderEvent event) {
        int length = event.getComponentLocator() == null ? 0 : 1;
        length += event.getArgs().length;
        String[] args = new String[length];
        int dif = 0;
        if (event.getComponentLocator() != null) {
            args[0] = event.getComponentLocator();
            dif = 1;
        }
        for (int i = 0; i < event.getArgs().length; i++) {
            String arg = event.getArgs()[i];
            args[i + dif] = arg;
        }
        play(event.getCommand(), args);
    }
}

package com.github.srec.play;

import com.github.srec.play.exception.TimeoutException;
import com.github.srec.play.exception.UnsupportedCommandException;
import com.github.srec.play.jemmy.*;
import com.github.srec.play.jemmy.command.*;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which plays srec commands.
 * 
 * @author Victor Tatai
 */
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
        return e;
    }

    public void addCommand(Command cmd) {
        commandMap.put(cmd.getName(), cmd);
    }

    public void init(Container... ignored) {
        addCommand(new ClickCommand());
        addCommand(new TypeCommand());
        addCommand(new TypeSpecialCommand());
        addCommand(new WindowActivateCommand());
        addCommand(new DialogActivateCommand());
        addCommand(new SelectCommand());
        addCommand(new CloseCommand());
        addCommand(new AssertCommand());
        addCommand(new FindCommand());
        addCommand(new PauseCommand());

        JemmyDSL.init(ignored);
    }
}

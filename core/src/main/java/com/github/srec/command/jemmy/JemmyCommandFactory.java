package com.github.srec.command.jemmy;

import com.github.srec.command.*;

/**
 * A command factory which produces Jemmy commands.
 *
 * @author Victor Tatai
 */
public class JemmyCommandFactory extends BaseCommandFactory {
    @Override
    public EventCommand buildEventCommand(String command, String... params) {
        if ("assert".equals(command)) {
            return new AssertCommand(params[0], null, params[1]);
        } else if ("click".equals(command)) {
            return new ClickCommand(params[0], null);
        } else if ("close".equals(command)) {
            return new CloseCommand(params[0], null);
        } else if ("dialog_activate".equals(command)) {
            return new DialogActivateCommand(params[0], null);
        } else if ("find".equals(command)) {
            return new FindCommand(params[0], null, params[1], params[2]);
        } else if ("pause".equals(command)) {
            return new PauseCommand();
        } else if ("select".equals(command)) {
            return new SelectCommand(params[0], null, params[1]);
        } else if ("type".equals(command)) {
            return new TypeCommand(params[0], null, params[1]);
        } else if ("type_special".equals(command)) {
            return new TypeSpecialCommand(params[0], null, params[1]);
        } else if ("window_activate".equals(command)) {
            return new WindowActivateCommand(params[0], null);
        }
        throw new UnsupportedCommandException("Unsupported command " + command);
    }
}

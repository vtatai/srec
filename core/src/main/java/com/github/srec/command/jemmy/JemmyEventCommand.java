package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.EventCommand;
import com.github.srec.play.exception.TimeoutException;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.awt.*;

/**
 * @author Victor Tatai
 */
public abstract class JemmyEventCommand extends EventCommand {
    protected JemmyEventCommand(String name, String componentLocator, Component component, String... params) {
        super(name, componentLocator, component, params);
    }

    protected JemmyEventCommand(String name, String componentLocator, Component component, boolean collapseMultiple, String... params) {
        super(name, componentLocator, component, collapseMultiple, params);
    }

    @Override
    public void run() {
        try {
            runJemmy();
        } catch (JemmyException e) {
            if (e instanceof TimeoutExpiredException) throw new TimeoutException(this, params, e);
            throw new UnsupportedFeatureException(e.getMessage());
        }
    }

    /**
     * Method which should be overridden by subclasses which run Jemmy commands. It is called by this class run method
     * and handles the exception translation.
     *
     * @throws JemmyException The exception which may need to be translated
     */
    protected abstract void runJemmy() throws JemmyException;
}

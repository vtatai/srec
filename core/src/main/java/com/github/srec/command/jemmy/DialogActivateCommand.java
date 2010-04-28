package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.dialog;

/**
 * @author Victor Tatai
 */
public class DialogActivateCommand extends JemmyEventCommand {
    public DialogActivateCommand(String componentLocator, Component component) {
        super("dialog_activate", componentLocator, component);
    }

    @Override
    public void runJemmy() {
        dialog(componentLocator);
    }
}
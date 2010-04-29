package com.github.srec.rec.command;

import com.github.srec.command.Command;
import com.github.srec.command.DefCommand;
import com.github.srec.command.CommandSerializer;
import com.github.srec.command.jemmy.ClickCommand;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Victor Tatai
 */
@Test
public class SerializerTest {
    public void test() throws IOException {
        String fileName = System.getProperty("java.io.tmpdir") + File.separator + "test.rb";
        List<Command> commands = new ArrayList<Command>();
        DefCommand defCommand = new DefCommand("m1");
        defCommand.add(new ClickCommand("textField", null));
        commands.add(defCommand);
        CommandSerializer.write(new File(fileName), commands);

        commands = CommandSerializer.read(new File(fileName));
        assertEquals(commands.size(), 1);
        assertTrue(commands.get(0) instanceof DefCommand);
        defCommand = (DefCommand) commands.get(0);
        assertEquals(defCommand.getName(), "m1");
    }
}

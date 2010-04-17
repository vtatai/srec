package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;
import org.netbeans.jemmy.operators.JComponentOperator;

import static com.github.srec.play.jemmy.JemmyDSL.assertText;
import static com.github.srec.play.jemmy.JemmyDSL.find;

public class FindCommand implements Command {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public void run(String... params) {
        if (params.length != 3) throw new IllegalParametersException("Missing parameters to find");
        find(params[0], params[1], params[2]);
    }

    @Override
    public String toString() {
        return getName();
    }
}
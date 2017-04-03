package com.polytech.smtp.server.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public abstract class MethodCommand extends Method
{
    protected List<String> commands;

    public MethodCommand(String[] commands) {
        this(new ArrayList<String>(Arrays.asList(commands)));
    }

    public MethodCommand(List<String> commands) {
        super();
        this.commands = new ArrayList<String>();
        for(String command : commands)
        {
            this.commands.add(command.toUpperCase());
        }
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean process(String command, List<String> lines)
    {
        log("incomming commands : " + command);
        for(String com : this.commands) {
            log("method commands : "+com);
        }

        if (this.commands.contains(command.toUpperCase())) {
            if (!this.processCommand(lines))
                log("error process command");
            return true;
        }else {
            log("method not match");
            return false;
        }
    }

    public abstract boolean processCommand(List<String> lines);
}

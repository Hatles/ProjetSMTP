package com.polytech.smtp.server.method;

import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public abstract class MethodCommand extends Method
{
    protected String command;

    public MethodCommand(String command) {
        super();
        this.command = command.toUpperCase();
    }

    public String getCommand() {
        return command;
    }

    public boolean process(String command, List<String> lines)
    {
        log("incomming command : "+command);
        log("method command : "+this.command);
        if(command.equals(this.command))
        {
            if(!this.processCommand(lines))
                log("error process command");
            return true;
        }
        else
        {
            log("method not match");
            return false;
        }
    }

    public abstract boolean processCommand(List<String> lines);
}

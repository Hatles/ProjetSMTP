package com.polytech.smtp.server.method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 16/02/2017.
 */
public class ConnectionMethod extends SMTPMethod
{
    public ConnectionMethod() {
        super(new String[] { "HELO","EHLO" });
    }

    @Override
    public boolean processCommand(List<String> lines)
    {
        for(String line : lines)
        {
                String[] helo = line.split(" ");
                if(helo.length != 2) {
                    try {
                        send501();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }
                String name = helo[1];

                try {
                    communication.clientConnected(name);
                    send250("-"+communication.getServerName()+" greets "+name);
                } catch (Exception e){
                    try {
                        send500();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }
                communication.setStatus("Waiting Mail");
                return true;
        }

        log(lines.get(0));
        return false;
    }


}

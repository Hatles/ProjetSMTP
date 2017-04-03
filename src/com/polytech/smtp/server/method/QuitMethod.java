package com.polytech.smtp.server.method;

import java.io.IOException;
import java.util.List;

/**
 * Created by corentinmarechal on 28/02/2017.
 */
public class QuitMethod extends SMTPMethod {

    public QuitMethod() {
        super("QUIT");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        if (communication.getName().isEmpty())
                return false;
        for(String line : lines)
        {
            if(line.trim().toUpperCase().equals("QUIT"))
            {
                try {
                    sendOK("dewey POP3 server signing off");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                communication.close();
                return true;
            }

        }
        return false;
    }
}

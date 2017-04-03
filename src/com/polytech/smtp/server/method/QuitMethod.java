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
                    send221();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                communication.close();
                communication.setStatus("Disconnected");
                return true;
            }

        }
        return false;
    }
}

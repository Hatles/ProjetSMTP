package com.polytech.smtp.server.method;
import com.Utils;
import com.stockage.Message;
import com.stockage.Stockage;
import com.stockage.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by corentinmarechal on 06/03/2017.
 */
public class MailMethod extends SMTPMethod {

    public MailMethod() {
        super("MAIL");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        if (communication.getName().isEmpty())
            return false;
        for(String line : lines)
        {
            MAIL FROM:<userx@y.foo.org>
            String[] mail = line.split(" ");
            if(mail.length != 2) {
                try {
                    send501();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return false;
            }
            String[] mail = line.split(":");

            String name = helo[1];
            if(line.trim().toUpperCase().equals("MAIL"))
            {
                try {
                    send221();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                communication.close();
                communication.setStatus("WAITING RECEPTION DATE");
                return true;
            }

        }
        return false;
    }

}

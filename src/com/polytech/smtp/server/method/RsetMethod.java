package com.polytech.smtp.server.method;

import java.io.IOException;
import java.util.List;

/**
 * Created by corentinmarechal on 24/04/2017.
 */
public class RsetMethod extends SMTPMethod {

    public MailMethod() {
        super("RSET");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        try {
            communication.setStatus("waiting_mail");
            send250("OK");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
}

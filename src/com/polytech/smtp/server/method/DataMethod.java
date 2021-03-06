package com.polytech.smtp.server.method;

import com.polytech.smtp.server.mail.Mailer;
import com.polytech.smtp.server.stockage.Stockage;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 24/04/2017.
 */
public class DataMethod extends SMTPMethod
{
    public DataMethod() {
        super("DATA");
    }

    @Override
    public boolean processCommand(List<String> lines)
    {
       Mailer mailer = Mailer.getInstance();
        if(mailer.hasRcpt())
        {
            communication.setStatus("receive");
            try {
                send354();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                send500();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

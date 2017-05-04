package com.polytech.smtp.server.method;

import com.polytech.smtp.server.mail.Mailer;
import com.polytech.smtp.server.stockage.Stockage;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 24/04/2017.
 */
public class DataReceiveMethod extends SMTPMethod
{
    public DataReceiveMethod() {
        super("");
    }

    public boolean process(String command, List<String> lines)
    {
        this.processCommand(lines);
        return true;
    }

    @Override
    public boolean processCommand(List<String> lines)
    {
        Mailer mailer = Mailer.getInstance();
        if(mailer.hasRcpt())
        {
            boolean end = false;
            for (String line : lines) {

                if(!line.equals("."))
                    mailer.data(line);
                else
                {
                    Stockage.getInstance().addMessage(mailer.getFrom(), mailer.getTo(), mailer.getData());
                    communication.setStatus("waiting_mail");
                    try {
                        send250("OK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    end = true;
                    break;
                }
            }

            if(!end)
            {
                try {
                    send250("OK");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

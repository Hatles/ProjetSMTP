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
            boolean lastLineEmpty = false;
            boolean lastLineDot = false;
            int i = 1;
            for (String line : lines)
            {
                if(!line.equals("."))
                {
                    if(line.equals(""))
                    {
                        mailer.data("");
                        lastLineEmpty = true;
                    }
                    else
                    {
                        lastLineEmpty = false;

                        if(lastLineDot)
                        {
                            mailer.data(".");
                            lastLineDot = false;
                        }
                        mailer.data(line);
                    }
                }
                else
                {
                    if(!lastLineDot)
                    {
                        lastLineDot = true;
                        lastLineEmpty = true;
                    }
                }
            }

            if(lastLineDot)
            {
                if(lastLineEmpty)
                {
                    Stockage.getInstance().addMessage(mailer.getFrom(), mailer.getTo(), mailer.getData());
                    Mailer.getInstance().reset();
                    communication.setStatus("waiting_mail");
                    try {
                        send250("OK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        send250("OK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            else
            {
                try {
                    send500();
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

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
public class RetrMethod extends SMTPMethod {

    public RetrMethod() {
        super("RETR");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        if (communication.getName().isEmpty())
            return false;

        String[] retr = lines.get(0).split(" ");
        if (retr.length != 2)
            return false;
        int num = 0;
        try {
            num = Integer.valueOf(retr[1]);
        } catch (Exception e) {
            sendError("no such message");
            return false;
        }
        try {
            User user = Stockage.getInstance().getUserBank().getUser(communication.getName());
            if(user.getMessages().size()<num || num<=0){
                sendError("no such message");
                return false;
            }
            Message message = user.getMessage(num);
            String msg = Utils.buildMessage(message);
            writeLine("+OK "+msg.length()+" octets");
            send(msg);
        } catch (IOException e) {
            try {
                sendERR("");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            try {
                sendERR("");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return false;
        }

        return true;
    }

    void sendError(String mess){
        try {
            sendERR(mess);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

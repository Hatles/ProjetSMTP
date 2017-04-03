package com.polytech.smtp.server.method;

import com.Utils;
import com.stockage.Message;
import com.stockage.Stockage;
import com.stockage.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by corentinmarechal on 06/03/2017.
 */
public class StatMethod extends SMTPMethod {

    public StatMethod() {
        super("STAT");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        if (communication.getName().isEmpty())
            return false;

        try {
            User user = Stockage.getInstance().getUserBank().getUser(communication.getName());
            List<Message> messages = user.getMessages();
            int length = 0;
            for(Message message : messages){
                length+= Utils.buildMessage(message).getBytes(StandardCharsets.UTF_8).length;
            }
            sendOK(messages.size() + " " + length);
        } catch (IOException e) {
            try {
                sendERR("");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (Exception e){
            try {
                sendERR("");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return false;
        }

        return false;
    }

}

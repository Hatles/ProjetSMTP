package com.polytech.smtp.server.method;

import com.polytech.smtp.server.mail.Mailer;
import com.polytech.smtp.server.stockage.Stockage;
import com.polytech.smtp.server.stockage.User;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by corentinmarechal on 06/03/2017.
 */
public class MailMethod extends SMTPMethod {

    public MailMethod() {
        super("MAIL");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        for (String line : lines) {
            String[] mail = line.split(":");
            if (mail.length != 2) {
                try {
                    send500();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return false;
            }

            if (mail[0].equals("MAIL FROM")) {
                boolean syntaxtTest = mail[1].matches("<\\w(?:[-_.]?\\w)*@\\w(?:[-_.]?\\w)*\\.(?:[a-z]{2,4})>");
                if (syntaxtTest) {
                    try {
                        send500();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return false;
                }

                try {
                    Pattern p = Pattern.compile("\\w(?:[-_.]?\\w)*@\\w(?:[-_.]?\\w)*\\.(?:[a-z]{2,4})");
                    Matcher email = p.matcher(mail[1]);
                    User user =Stockage.getInstance().getUserBank().getUser(email.group(0));
                    Mailer.getInstance().from(user.getName());
                    communication.setStatus("waiting_data");
                    send250("OK");
                    return true;
                } catch (Exception e) {
                    try {
                        send550Mail();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    return false;
                }
            } else {
                try {
                    send500();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}

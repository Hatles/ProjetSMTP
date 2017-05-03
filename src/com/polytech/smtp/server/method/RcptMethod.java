package com.polytech.smtp.server.method;

        import com.polytech.smtp.server.mail.Mailer;
        import com.polytech.smtp.server.stockage.Stockage;
        import com.polytech.smtp.server.stockage.User;

        import java.io.IOException;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

/**
 * Created by corentinmarechal on 06/04/2017.
 */
public class RcptMethod extends SMTPMethod {

    public RcptMethod() {
        super("RCPT");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        for (String line : lines) {
            try {
                String[] rcpt = line.split(":");
                if (rcpt.length != 2) {
                    throw new IOException();
                }

                if (rcpt[0].equals("RCPT TO")) {
                    boolean syntaxtTest = rcpt[1].matches("<\\w(?:[-_.]?\\w)*@\\w(?:[-_.:]?\\w)*>");
                    if (!syntaxtTest) {
                        throw new IOException();
                    }

                    try {
                        String[] email = rcpt[1].split("@");
                        String username = email[0].replace("<", "");
                        String servername = email[1].replace(">", "");
                        if (!servername.equals(communication.getServerName()))
                            throw new Exception();
                        User user = Stockage.getInstance().getUserBank().getUser(username);
                        Mailer.getInstance().to(user.getName());
                        send250(" OK");
                        return true;
                    } catch (Exception e) {
                        try {
                            send550Rcpt();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                        return false;
                    }
                } else {
                    throw new IOException();
                }
            } catch (IOException ioe) {
                try {
                    send500();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

}

package com.polytech.smtp.server.method;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public abstract class SMTPMethod extends MethodCommand
{
    public SMTPMethod(List<String> commands) {
        super(commands);
    }

    public SMTPMethod(String[] commands) {
        super(commands);
    }

    public SMTPMethod(String command) {
        super(command);
    }

    public void sendResponse(String response, String message) throws IOException {
        this.send(response + " " + message);
    }

    public void send221() throws IOException {
        this.send("221 "+communication.getServerName()+" Service closing transmission channel");
    }

    public void send250(String message) throws IOException {
        this.send("250 "+message);
    }

    public void send354() throws IOException {
        this.send("354 Start Mail Input ; end with <CRLF>.<CRLF>");
    }

    public void send500() throws IOException {
        this.send("500 syntax error command unrocognized");
    }

    public void send501() throws IOException {
        this.send("501 syntax error in parameters or arguments");
    }

    public void send550Mail() throws IOException {
        this.send("550 Requested action not taken: mailbox unavailable");
    }

    public void send550Rcpt() throws IOException {
        this.send("550 no such user here");
    }
}

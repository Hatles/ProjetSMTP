package com.polytech.smtp.server.method;

import java.io.IOException;

/**
 * Created by kifkif on 28/02/2017.
 */
public abstract class SMTPMethod extends MethodCommand
{
    public SMTPMethod(String command) {
        super(command);
    }

    public void sendOK(String message) throws IOException {
        this.sendResponse("+OK", message);
    }

    public void sendERR(String message) throws IOException {
        this.sendResponse("-ERR", message);
    }

    public void sendResponse(String response, String message) throws IOException {
        this.send(response + " " + message);
    }
}

package com.polytech.smtp.server.method;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 03/04/2017.
 */
public class NoopMethod extends SMTPMethod {
    public NoopMethod() {
        super("NOOP");
    }

    @Override
    public boolean processCommand(List<String> lines) {
        try {
            send250(" OK");
            return true;
        } catch (IOException e) {
            log(e.getMessage());
            return false;
        }
    }
}

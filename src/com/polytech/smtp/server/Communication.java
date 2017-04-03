package com.polytech.smtp.server;

import java.io.File;
import java.io.IOException;

/**
 * Created by kifkif on 16/02/2017.
 */
public interface Communication {
    public void writeData(File file) throws IOException;

    public void writeLine(String message) throws IOException;

    public void write(String message) throws IOException;

    public void send(String message) throws IOException;

    public void send() throws IOException;

    public void log(String message);
}

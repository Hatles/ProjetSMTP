package com.polytech.smtp.server.method;

import com.Communication;
import com.SMTPCommunication;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Method implements Communication
{
	protected SMTPCommunication communication;

	public Method()
	{
    }

	public Communication getCommunication() {
		return communication;
	}

	public void setCommunication(SMTPCommunication communication) {
		this.communication = communication;
	}

//	protected abstract String onError();

	public abstract boolean process(String command, List<String> lines);

    @Override
    public void writeData(File file) throws IOException {
        this.communication.writeData(file);
    }

    @Override
    public void writeLine(String message) throws IOException {
        this.communication.writeLine(message);
    }

    @Override
    public void write(String message) throws IOException {
        this.communication.write(message);
    }

    @Override
    public void send(String message) throws IOException {
        this.communication.send(message);
    }

    @Override
    public void send() throws IOException {
        this.send("");
    }

    @Override
    public void log(String message) {
        this.communication.log(message);
    }
}

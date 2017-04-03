package com.polytech.smtp.server.status;

import com.polytech.smtp.server.Communication;
import com.polytech.smtp.server.SMTPCommunication;
import com.polytech.smtp.server.method.Method;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 03/04/2017.
 */
public class Status implements Communication{
    private String name;
    private List<Method> methods;
    private SMTPCommunication communication;

    public Status(String name, List<Method> methods) {
        this.name = name;
        this.methods = methods;
    }

    public Status(String name) {
        this(name, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public void addMethod(Method method)
    {
        if(method != null)
            methods.add(method);
    }

    public void setCommunication(SMTPCommunication communication) {
        this.communication = communication;
        for (Method method: methods) {
            method.setCommunication(communication);
        }
    }

    public boolean processCommand(String command, List<String> lines) {
        for (Method method : methods) {
            if(method.process(command, lines))
                return true;
        }

        this.onUnkownCommand(command, lines);
        return false;
    }

    private void onUnkownCommand(String command, List<String> lines) {
        log("unknown command received from client : " + command);
    }

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

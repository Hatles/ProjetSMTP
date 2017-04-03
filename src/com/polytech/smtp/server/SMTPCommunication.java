package com.polytech.smtp.server;

import com.factory.CommunicationFactory;
import com.method.Method;
import com.polytech.smtp.utils.Utils;
import com.stockage.Stockage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 15/02/2017.
 */
public class SMTPCommunication extends CommunicationRunnable
{
    private String name;
    private String timestamp;
    private boolean connected;

    private Method connectionMethod;
    private List<Method> methods;

    public SMTPCommunication(Server server, Socket socket, CommunicationFactory factory)
    {
        super(server, socket);
        methods = new ArrayList<>();
        connected = false;
        factory.buildCommunication(this);
        Stockage.getInstance().setServer(server);
    }

    @Override
    protected void onStart() {
        try {
            timestamp= Utils.createTimestamp(server.getServerName());
            this.send("+OK POP3 server ready "+this.getServer().getServerName()+" "+timestamp);
        } catch (IOException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onClientCommunication(List<String> lines)
    {
        String command = Utils.getCommand(lines);
        if(!connected)
            this.processConnecting(command, lines);
        else
            this.processRequest(command,  lines);
    }



    private void processConnecting(String command, List<String> lines)
    {
        log("try process connecting");

        if(this.connectionMethod != null)
        {
            this.connectionMethod.process(command, lines);
        }
        else
        {
            log("connection method null");
//            log(e.getMessage());
//            e.printStackTrace();
            this.close();
        }
    }

    private void processRequest(String command, List<String> lines)
    {
        log("try process connecting");
        for (Method method : methods) {
            if(method.process(command, lines))
                return;
        }

        this.onUnkownCommand(command, lines);
    }

    private void onUnkownCommand(String command, List<String> lines)
    {
        log("unknown command received from client : " + command);
        //this.close();
    }

    protected String getTag()
    {
        return "[Client-"+id+"-"+name+"]";
    }

    @Override
    protected void onClose() {

    }

    public String getName() {
        return name;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Method getConnectionMethod() {
        return connectionMethod;
    }

    public void setConnectionMethod(Method connectionMethod) {
        connectionMethod.setCommunication(this);
        this.connectionMethod = connectionMethod;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public void addMethod(Method method)
    {
        method.setCommunication(this);
        this.methods.add(method);
    }

    public void removeMethod(Method method)
    {
        this.methods.remove(method);
    }

    public void clientConnected(String name)
    {
        this.setName(name);
        this.setConnected(true);
    }
}

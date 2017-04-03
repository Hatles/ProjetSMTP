package com.polytech.smtp.server;

import com.polytech.smtp.server.factory.CommunicationFactory;
import com.polytech.smtp.server.method.Method;
import com.polytech.smtp.server.status.Status;
import com.polytech.smtp.server.stockage.Stockage;
import com.polytech.smtp.utils.Utils;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by kifkif on 15/02/2017.
 */
public class SMTPCommunication extends CommunicationRunnable
{
    private String name;
    private String timestamp;
    private boolean connected;

    private Status status;
    private Map<String, Status> statusMap;

    public SMTPCommunication(Server server, Socket socket, CommunicationFactory factory)
    {
        super(server, socket);
        statusMap = new HashMap<>();
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
        this.processRequest(command,  lines);
    }

    private void processRequest(String command, List<String> lines)
    {
        log("try process command");
        status.processCommand(command, lines);
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

    public Map<String, Status> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, Status> statusMap) {
        this.statusMap = statusMap;
    }

    public void addStatus(Status status)
    {
        status.setCommunication(this);
        this.statusMap.put(status.getName(), status);
    }

    public void removeStatus(Status status)
    {
        this.statusMap.remove(status);
    }
    public void removeStatus(String status)
    {
        this.statusMap.remove(status);
    }

    public void clientConnected(String name)
    {
        this.setName(name);
        this.setConnected(true);
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public void setStatus(String status)
    {
        try {
            this.status = this.getStatus(status);
        }
        catch (NoSuchElementException e)
        {
            log(e.getMessage());
        }
    }

    public Status getStatus(String status) throws NoSuchElementException
    {
        Status statusObj = statusMap.get(status);
        if(statusObj != null)
            return statusObj;
        else throw new NoSuchElementException();
    }
}

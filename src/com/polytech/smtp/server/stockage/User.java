package com.polytech.smtp.server.stockage;

import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public class User
{
    private String  name;
    private String control;
    private List<Message> messages;

    public User(String name, String control, List<Message> messages)
    {
        this.name = name;
        this.control = control;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Message getMessage(int i)
    {
        return this.messages.get(i-1);
    }

    public void addMessage(Message message)
    {
        this.messages.add(message);
    }
}

package com.polytech.smtp.server.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 24/04/2017.
 */
public class Mailer
{
    private static Mailer instance;

    public static Mailer getInstance()
    {
        if(instance == null)
            instance = new Mailer();
        return instance;
    }

    private String from;
    private List<String> to;
    private StringBuilder data;

    protected Mailer()
    {
        to = new ArrayList<>();
        data = new StringBuilder();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void from(String from) {
        setFrom(from);
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void to(String to) {
        this.to.add(to);
    }

    public String getData() {
        return data.toString();
    }

    public void setData(StringBuilder data) {
        this.data = data;
    }

    public void data(String data)
    {
        this.data.append(data);
    }

    public void reset()
    {
        from = null;
        to = new ArrayList<>();
        data = new StringBuilder();
    }

    public boolean hasRcpt()
    {
        return from != null && !to.isEmpty();
    }
}

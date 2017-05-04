package com.polytech.smtp.server.stockage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 13/03/2017.
 */
public class Message {
    private List<Header> headers;
    private String message;

    public Message(List<Header> headers, String message)
    {
        this.headers = headers;
        this.message = message;
    }

    public Message()
    {
        this(new ArrayList<Header>(), "undefined");
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void addHeader(Header header)
    {
        this.headers.add(header);
    }

    public void addHeader(String title, String value)
    {
        this.addHeader(new Header(title, value));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("message", message);

        JSONArray list = new JSONArray();
        for(Header header : headers)
        {
            list.add(header.toJson());
        }


        obj.put("headers", list);

        return obj;
    }
}

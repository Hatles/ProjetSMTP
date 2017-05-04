package com.polytech.smtp.server.stockage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by kifkif on 13/03/2017.
 */
public class Header {
    private String title;
    private String value;

    public Header(String title, String value)
    {
        this.title = title;
        this.value = value;
    }

    public Header()
    {
        this("undefined", "undeifined");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put("title", title);
        obj.put("value", value);

        return obj;
    }
}

package com.polytech.smtp.server.stockage;


import com.polytech.smtp.server.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 28/02/2017.
 */
public class Stockage
{
    private static Stockage instance;

    public static synchronized Stockage getInstance()
    {
        if(instance == null)
            instance = new Stockage();

        return instance;
    }

    protected Server server;
    protected JSONParser parser;

    protected Stockage()
    {
         parser = new JSONParser();
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public UserBank getUserBank()
    {
        UserBank bank = new UserBank();
        try {

            Object obj = parser.parse(new FileReader(server.getRootDir()));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray userList = (JSONArray) jsonObject.get("users");
            for (JSONObject jsonUser : (Iterable<JSONObject>) userList) {
                String name = (String) jsonUser.get("name");
                String control = (String) jsonUser.get("control");
                JSONArray messageList = (JSONArray) jsonUser.get("messages");
                List<Message> messages = new ArrayList<>();
                for (JSONObject jsonMessage : (Iterable<JSONObject>) messageList) {
                    String message = (String) jsonMessage.get("message");
                    JSONArray headersList = (JSONArray) jsonMessage.get("headers");
                    List<Header> headers = new ArrayList<>();
                    for (JSONObject jsonHeader : (Iterable<JSONObject>) headersList) {
                        String title = (String) jsonHeader.get("title");
                        String value = (String) jsonHeader.get("value");
                        headers.add(new Header(title, value));
                    }
                    messages.add(new Message(headers, message.replaceAll("<CR>", "\r").replaceAll("<LF>", "\n")));
                }

                User user = new User(name, control, messages);
                bank.addUser(user);
//                System.out.println(name+"|"+control+"|"+messages.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bank;
    }
}

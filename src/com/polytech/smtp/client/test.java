package com.polytech.smtp.client;

import java.util.Date;

/**
 * Created by kifkif on 27/03/2017.
 */
public class test {

    public static void main(String [] args){
        Message myMess = new Message();
        myMess.setDate(new Date());
        myMess.setSubject("test");
        myMess.setBody("test");

        try {
            myMess.addRecipient("toto@localhost:5555");
            myMess.addRecipient("toto@134.214.117.188:1024");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Client myC = new Client(myMess,"toto","localhost:5555");

        myC.run();
    }
}

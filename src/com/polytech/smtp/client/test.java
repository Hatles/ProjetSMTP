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

        myMess.addRecipient("toto@localhost:5555");

        Client myC = new Client(myMess,"toto","localhost:5555");

        myC.run();
    }
}

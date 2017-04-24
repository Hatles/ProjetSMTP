package com.polytech.smtp.client;

import java.io.BufferedReader;
import java.io.IOException;


public class ServerAwnser {
    private String code;
    private String infos;

    public static ServerAwnser readAwnser (BufferedReader input){
        StringBuilder sb = new StringBuilder();

        String data;
        do{
            try {
                data = input.readLine();

                sb.append(data +" ");

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }while(data.length()>0);
        ServerAwnser awnser = new ServerAwnser();
        awnser.code  = sb.substring(0,3);
        awnser.infos = sb.substring(3);

        return awnser;
    }

    public String getCode() {
        return code;
    }
}

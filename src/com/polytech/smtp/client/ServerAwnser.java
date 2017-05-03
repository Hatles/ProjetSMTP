package com.polytech.smtp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ServerAwnser {
    private String code;
    private String infos;

    public static ServerAwnser readAwnser (BufferedReader input){
        StringBuilder sb = new StringBuilder();

        String data;
        try {
            do{
                data = input.readLine();
                sb.append(data);
            }
            while(input.ready());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerAwnser awnser = new ServerAwnser();
        awnser.code  = sb.substring(0,3);
        awnser.infos = sb.substring(3);

        return awnser;
    }

    public String getCode() {
        return code;
    }
}

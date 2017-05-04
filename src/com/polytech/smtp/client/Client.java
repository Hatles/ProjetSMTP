package com.polytech.smtp.client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by lafay on 03/04/2017.
 */
public class Client {

    private Message messageToSend;
    private ClientState state;
    private String user;
    private String domaine;

    public Client(Message messageToSend, String user, String domaine){

        this.messageToSend = messageToSend;
        this.user = user;
        this.domaine = domaine;
        this.state = ClientState.BUILD;

    }

    public void run(){
        for(Map.Entry<String, LinkedList<String>> serverRcpt : this.messageToSend.getRecipient().entrySet()){ // Pour chaque serveur dans les destinataires
            String [] serverInfo = serverRcpt.getKey().split(":");
            String server = serverInfo[0];
            int port = serverInfo.length == 2 ? Integer.parseInt(serverInfo[1]):25;

            //Tenative de connection
            Socket connection = null;
            BufferedReader input = null;
            BufferedWriter output = null;

            ServerAwnser awnser = null;
            boolean changeServer = false;
            while(!changeServer){
                switch (state){
                    case BUILD:
                        try {
                            connection = new Socket(server, port);
                            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        state = ClientState.CONNECTION;
                        break;
                    case DATA:
                        awnser = ServerAwnser.readAwnser(input);
                        if(!awnser.getCode().equals("354")){
                            //TODO error DATA
                            break;
                        }
                        sendCommand(output, messageToSend.getBody());
                        awnser = ServerAwnser.readAwnser(input);
                        if(!awnser.getCode().equals("250")){
                            //TODO error DATA
                            break;
                        }
                        sendCommand(output, "QUIT");
                        changeServer = true;
                        state = ClientState.CONNECTION;
                        break;
                    case MAILING:
                        awnser = ServerAwnser.readAwnser(input);
                        if(!awnser.getCode().equals("250")){
                            //TODO error réponse a MAIL
                            break;
                        }

                        int tryRCPT = 0;
                        for(String rcpt: serverRcpt.getValue()){
                            sendCommand(output, "RCPT TO:<"+rcpt+"@"+server+">\r\n");

                            awnser = ServerAwnser.readAwnser(input);
                            switch (awnser.getCode()){
                                case "250":
                                    tryRCPT++;
                                    break;
                                case "550":
                                    //TODO aficher erreur (utilisateur inconu ou autre)
                                    changeServer = true;
                                    break;
                            }
                        }
                        if(tryRCPT > 0 ){
                            sendCommand(output, "DATA\r\n");
                            state = ClientState.DATA;
                        }

                        break;
                    case WAITING:
                        awnser = ServerAwnser.readAwnser(input);
                        if(!awnser.getCode().equals("250")){
                            //TODO error management
                            break;
                        }
                        sendCommand(output,"MAIL FROM:<" + user+ "@" + domaine + ">\r\n");
                        state = ClientState.MAILING;
                        break;
                    case CONNECTION:
                        awnser = ServerAwnser.readAwnser(input);
                        switch (awnser.getCode()){
                            case "220":
                                //Envoi de EHLO
                                sendCommand(output,"EHLO " + domaine + "\r\n");
                                state = ClientState.WAITING;
                                break;
                            case "221":
                                try {
                                    connection.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            default:
                                //TODO error management
                        }


                        break;
                }
            }




            //connection réussie



        }
    }

    private static boolean sendCommand(BufferedWriter output, String command){
        System.out.println(command);
        try {
            output.write(command+"\r\n");
            output.flush();
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

}

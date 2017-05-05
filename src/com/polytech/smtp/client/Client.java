package com.polytech.smtp.client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

/**
 * Created by lafay on 03/04/2017.
 */
public class Client extends Observable{

    private Message messageToSend;
    private ClientState state;
    private String user;
    private String domaine;
    public String viewMessage = "";

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

            ServerAwnser answer;
            boolean changeServer = false;
            state = ClientState.BUILD;

            while(!changeServer){

                switch (state){

                    case BUILD:
                        try {
                            connection = new Socket(server, port);
                            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                        } catch (IOException e) {
                            viewMessage = "INTERNAL ERROR : " + e.getMessage() + "\n";
                            setChanged();
                            notifyObservers();
                            changeServer = true;
                            break;
                        }
                        state = ClientState.CONNECTION;
                        break;

                    case DATA:
                        answer = ServerAwnser.readAwnser(input);
                        if(!answer.getCode().equals("354")){
                            viewMessage = "No correct recipient found\n";
                            sendCommand(output, "RSET\r\n");
                            setChanged();
                            notifyObservers();
                            break;
                        }
                        sendCommand(output, messageToSend.toMailDATA(user));
                        answer = ServerAwnser.readAwnser(input);
                        if(!answer.getCode().equals("250")){
                            viewMessage = "Fail to send body\n";
                            setChanged();
                            notifyObservers();
                            break;
                        }
                        sendCommand(output, "QUIT\r\n");
                        changeServer = true;
                        viewMessage = "SUCCESS";
                        setChanged();
                        notifyObservers();
                        break;

                    case MAILING:
                        answer = ServerAwnser.readAwnser(input);
                        if(!answer.getCode().equals("250")){
                            viewMessage += "Fail to send MAIL command\n";
                            setChanged();
                            notifyObservers();
                            break;
                        }

                        int tryRCPT = 0;
                        for(String rcpt: serverRcpt.getValue()){
                            sendCommand(output, "RCPT TO:<"+rcpt+"@"+server+">\r\n");

                            answer = ServerAwnser.readAwnser(input);
                            switch (answer.getCode()){
                                case "250":
                                    tryRCPT++;
                                    break;
                                case "550":
                                    viewMessage += "Unknown user " + rcpt + "\n";
                                    setChanged();
                                    notifyObservers();
                                    changeServer = true;
                                    break;
                            }
                        }
                        if(tryRCPT > 0 ){
                            sendCommand(output, "DATA\r\n");
                            state = ClientState.DATA;
                            break;
                        }else{
                            sendCommand(output, "RSET\r\n");
                            viewMessage = "Aucun utilisateurs valide pour le serveur";
                            changeServer = true;
                            setChanged();
                            notifyObservers();
                        }
                        break;

                    case WAITING:
                        answer = ServerAwnser.readAwnser(input);
                        if(!answer.getCode().equals("250")){
                            sendCommand(output, "RSET\r\n");
                            viewMessage += "Server exception detected\n";
                            setChanged();
                            notifyObservers();
                            break;
                        }
                        sendCommand(output,"MAIL FROM:<" + user+ "@" + domaine + ">\r\n");
                        state = ClientState.MAILING;
                        break;

                    case CONNECTION:
                        answer = ServerAwnser.readAwnser(input);
                        switch (answer.getCode()){
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
                                    viewMessage = "INTERNAL ERROR : " + e.getMessage() + "\n";
                                    setChanged();
                                    notifyObservers();
                                }
                                continue;
                            default:
                                sendCommand(output, "RSET\r\n");
                                viewMessage = "INTERNAL ERROR : state not allow in this application\n" +
                                    "Check Domaines adresses, it should be like this : 'server:port'\n";
                                setChanged();
                                notifyObservers();
                                changeServer = true;
                        }
                        break;
                }
            }
        }

    }

    private static boolean sendCommand(BufferedWriter output, String command){
        System.out.println(command);
        try {
            output.write(command+"\r\n\r\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

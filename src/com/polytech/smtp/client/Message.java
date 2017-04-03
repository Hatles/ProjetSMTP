package com.polytech.smtp.client;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Message {

    private String subject;
    private Date date;
    private HashMap<String, LinkedList<String>> recipient;
    private String body;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HashMap<String, LinkedList<String>> getRecipient() {
        return recipient;
    }

    /**
     *
     * @param eMailReceipt Destinataire sous le format user@server:port
     */
    public void addRecipient(String eMailReceipt){
        String[]datas = eMailReceipt.split("@");
        if(!this.recipient.containsKey(datas[1])){
            this.recipient.put(datas[1],new LinkedList<>());
        }
        this.recipient.get(datas[1]).add(datas[0]);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body.endsWith("\r\n")?body:body+"\r\n";

    }

    public String toMailDATA(String fromMail){
        StringBuilder mailToString = new StringBuilder();

        //HEADER
        mailToString.append("From: " + fromMail +" <" + fromMail + ">\r\n");
        mailToString.append("To: ");
        for (Map.Entry<String ,LinkedList<String>> server : this.getRecipient().entrySet()){
            for(String rcpt : server.getValue()){
                mailToString.append(rcpt+" <"+rcpt+"@"+server.getKey().split(":")[0]+">, ");
            }
        }
        mailToString.deleteCharAt(mailToString.lastIndexOf(", "));
        mailToString.append("\r\n");

        mailToString.append("Subject: "+this.getSubject()+"\r\n");
        mailToString.append("Date:"+this.getDate().toString()+"\r\n");

        mailToString.append("\r\n");

        //BODY
        mailToString.append(this.getBody() + ".\r\n");



        return mailToString.toString();
    }






}

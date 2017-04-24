package com.polytech.smtp.client.gui;

import com.polytech.smtp.client.Client;
import com.polytech.smtp.client.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by lafay on 24/04/2017.
 */
public class ConstructionMail extends JFrame{
    private JPanel panel1;
    private JTextArea txtMailBody;
    private JTextField txtUser;
    private JTextField txtDomaine;
    private JTextField txtRcpt;
    private JButton btnSend;
    private JTextField txtSubject;
    private JPanel panError;
    private JTextArea textErrors;

    public ConstructionMail() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message myMessage = new Message();

                myMessage.setBody(txtMailBody.getText());
                myMessage.setSubject(txtSubject.getText());
                myMessage.setDate(new Date());

                String [] rcpts = txtRcpt.getText().trim().split(";");
                for(String rcpt : rcpts){
                    myMessage.addRecipient(rcpt);
                }


                Client myClient = new Client(myMessage,txtUser.getText(),txtDomaine.getText());
                myClient.run();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ConstructionMail");
        frame.setContentPane(new ConstructionMail().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}

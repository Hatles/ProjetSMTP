package com.polytech.smtp.client.gui;

import com.polytech.smtp.client.Client;
import com.polytech.smtp.client.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lafay on 24/04/2017.
 */
public class ConstructionMail  extends JFrame implements Observer {
    private JPanel panel1;
    private JTextArea txtMailBody;
    private JTextField txtUser;
    private JTextField txtDomaine;
    private JTextField txtRcpt;
    private JButton btnSend;
    private JTextField txtSubject;
    private JPanel panError;
    private JTextArea textOut;

    Client myClient;
    ConstructionMail crMAil;

    public ConstructionMail(){
        crMAil = this;
        btnSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Message myMessage = new Message();

                myMessage.setBody(txtMailBody.getText());
                myMessage.setSubject(txtSubject.getText());
                myMessage.setDate(new Date());

                String [] rcpts = txtRcpt.getText().trim().split(";");

                try {

                    for(String rcpt : rcpts){
                        myMessage.addRecipient(rcpt);
                    }

                } catch (Exception e1) {
                    textOut.setText(e1.getMessage());
                }

                myClient = new Client(myMessage, txtUser.getText(), txtDomaine.getText());

                myClient.addObserver(crMAil);

                myClient.run();
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ConstructionMail");

        ConstructionMail view = new ConstructionMail();

        frame.setContentPane(view.panel1);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void update(Observable o, Object arg) {
        textOut.setText(textOut.getText()+ myClient.viewMessage);
    }
}

package com.polytech.smtp.server.factory;

import com.SMTPCommunication;
import com.method.*;

/**
 * Created by kifkif on 16/02/2017.
 */
public class SMTPCommunicationFactory implements CommunicationFactory {

    @Override
    public void buildCommunication(SMTPCommunication communication)
    {
        communication.setConnectionMethod(new ConnectionMethod());
        communication.addMethod(new QuitMethod());
        communication.addMethod(new StatMethod());
        communication.addMethod(new RetrMethod());
//        communication.addMethod(new ConnectionMethod());
    }
}

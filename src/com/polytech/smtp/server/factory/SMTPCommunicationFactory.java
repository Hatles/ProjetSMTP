package com.polytech.smtp.server.factory;

import com.polytech.smtp.server.SMTPCommunication;
import com.polytech.smtp.server.method.ConnectionMethod;
import com.polytech.smtp.server.method.Method;
import com.polytech.smtp.server.status.Status;

/**
 * Created by kifkif on 16/02/2017.
 */
public class SMTPCommunicationFactory implements CommunicationFactory {

    @Override
    public void buildCommunication(SMTPCommunication communication)
    {
        Method connection = new ConnectionMethod();
//        Method rset = new RESTMethod();

        Status authorization = new Status("authorization");
        authorization.addMethod(connection);

        Status waitingMail = new Status("waiting_mail");
        authorization.addMethod(connection);

        communication.addStatus(authorization);
        communication.addStatus(waitingMail);

        communication.setStatus("authorization");
    }
}

package com.polytech.smtp.server.factory;

import com.polytech.smtp.server.SMTPCommunication;
import com.polytech.smtp.server.method.*;
import com.polytech.smtp.server.status.Status;

/**
 * Created by kifkif on 16/02/2017.
 */
public class SMTPCommunicationFactory implements CommunicationFactory {

    @Override
    public void buildCommunication(SMTPCommunication communication)
    {
        Method connection = new ConnectionMethod();
        Method noop = new NoopMethod();
        Method mail = new MailMethod();
        Method rcpt = new RcptMethod();
        Method quit = new QuitMethod();
//        Method rset = new RESTMethod();

        Status authorization = new Status("authorization");
        authorization.addMethod(connection);
        authorization.addMethod(noop);
        authorization.addMethod(quit);

        Status waitingMail = new Status("waiting_mail");
        authorization.addMethod(noop);
        authorization.addMethod(mail);
        authorization.addMethod(quit);

        Status waitingData = new Status("waiting_data");
        authorization.addMethod(noop);
        authorization.addMethod(rcpt);

        Status receive = new Status("receive");
        authorization.addMethod(noop);

        communication.addStatus(authorization);
        communication.addStatus(waitingMail);
        communication.addStatus(waitingData);
        communication.addStatus(receive);

        communication.setStatus("authorization");
    }
}

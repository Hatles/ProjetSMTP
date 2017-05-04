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
        Method rset = new RsetMethod();
        Method quit = new QuitMethod();
        Method dataReceive = new DataReceiveMethod();
        Method data = new DataMethod();

        Status authorization = new Status("authorization");
        authorization.addMethod(connection);
        authorization.addMethod(noop);
        authorization.addMethod(quit);

        Status waitingMail = new Status("waiting_mail");
        waitingMail.addMethod(noop);
        waitingMail.addMethod(mail);
        waitingMail.addMethod(quit);

        Status waitingData = new Status("waiting_data");
        waitingData.addMethod(noop);
        waitingData.addMethod(rcpt);
        waitingData.addMethod(rset);
        waitingData.addMethod(data);

        Status receive = new Status("receive");
        receive.addMethod(noop);
        receive.addMethod(rset);
        receive.addMethod(dataReceive);

        communication.addStatus(authorization);
        communication.addStatus(waitingMail);
        communication.addStatus(waitingData);
        communication.addStatus(receive);

        communication.setStatus("authorization");
    }
}

package com.polytech.smtp.server.factory;

import com.SMTPCommunication;

/**
 * Created by kifkif on 16/02/2017.
 */
public interface CommunicationFactory
{
    void buildCommunication(SMTPCommunication communication);
}

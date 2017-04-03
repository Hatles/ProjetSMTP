package com.polytech.smtp.client;

/**
 * Created by lafay on 03/04/2017.
 */
public enum ClientState {
    BUILD,
    CONNECTION,
    WAITING,
    MAILING,
    DATA;
}

package ru.sbrf.lab.security;

import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import oracle.security.xs.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class APLSession {

    private Session rasSession;

    public APLSession (Session rasSession) {
        this.rasSession = rasSession;
    }

    public Session getRasSession() {
        return rasSession;
    }

    public void setRasSession(Session rasSession) {
        this.rasSession = rasSession;
    }
}

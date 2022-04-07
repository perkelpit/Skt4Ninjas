package com.company.skt.lib;

public class Lock {
    
    public static final int WAIT = 0;
    public static final int NOTIFY = 1;
    public static final int NOTIFY_ALL = 2;
    
    public synchronized void syncWait() {
        try {
            this.wait();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void syncNotify() {
        this.notify();
    }
    
    public synchronized void syncNotifyAll() {
        this.notifyAll();
    }
    
}

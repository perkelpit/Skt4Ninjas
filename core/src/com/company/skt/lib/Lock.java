package com.company.skt.lib;

/** A wrapper class around a plane {@link Object} to ensure that Threads calling {@code wait(), notify() or
 * notifyAll() owns the monitor.}*/
public class Lock {
    
    public static final int WAIT = 0;
    public static final int NOTIFY = 1;
    public static final int NOTIFY_ALL = 2;
    
    public synchronized void syncWait() {
        try {
            this.wait();
        } catch(InterruptedException ignored) {}
    }

    public synchronized void syncWait(long ms) {
        try {
            this.wait(ms);
        } catch(InterruptedException ignored) {}
    }
    
    public synchronized void syncNotify() {
        this.notify();

    }
    
    public synchronized void syncNotifyAll() {
        this.notifyAll();
    }
    
}
